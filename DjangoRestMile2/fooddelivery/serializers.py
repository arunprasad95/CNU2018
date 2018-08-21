from rest_framework import serializers, status
from rest_framework.response import Response

from .models import *
from django.core.exceptions import ObjectDoesNotExist
from django.forms.models import model_to_dict

def create_cuisne(cname):
    try:
        COBJ = Cuisine.objects.get(name=cname)
    except ObjectDoesNotExist:
        COBJ = Cuisine(name=cname)
        COBJ.save()
    return Cuisine.objects.get(name=cname)
class RestaurantSerializer(serializers.ModelSerializer):

    cuisines = serializers.SlugRelatedField(many=True,
                                            slug_field='name',
                                            queryset=Cuisine.objects.all())

    class Meta:
        model = Restaurant
        fields = ('id', 'name', 'city', 'latitude', 'longitude', 'rating', 'is_open', 'cuisines')

    def to_internal_value(self, data):
        data['cuisines'] = map(lambda x:create_cuisne(x) , data['cuisines'])
        return data
    def validate(self, attrs):

        if attrs.keys() - self.initial_data.keys():
            raise serializers.ValidationError("Fields are absent")
        if 'latitude' not in attrs.keys():
            raise serializers.ValidationError("Fields are absent")
        if attrs['latitude'] > 90 or attrs['latitude'] < -90:
            raise serializers.ValidationError("Latitude range incorrect")
        if attrs['longitude'] > 180 or attrs['longitude'] < -180:
            raise serializers.ValidationError("Longitude range incorrect")
        if attrs['rating'] < 0 or attrs['rating'] > 9:
            raise serializers.ValidationError("Rating range incorrect")

        return attrs


class ItemSerializer(serializers.ModelSerializer):
    class Meta:
        model = Item
        fields = "__all__"
    def to_representation(self, instance):
        restaurant = model_to_dict(instance.restaurant_id)
        restaurant['cuisines'] = map(lambda x:x.name ,Cuisine.objects.filter(restaurant_id=instance.restaurant_id) )
        temp = {}
        temp["id"]=instance.id
        temp["name"]=instance.name
        temp["price"]=str(float(instance.price))
        temp["restaurant"]=restaurant
        return temp

    def validate(self, attrs):
        if attrs.keys() - self.initial_data.keys():
            raise serializers.ValidationError("Fields are absent")
        if attrs['price'] < 0:
            raise serializers.ValidationError("Price range incorrect")
        return attrs

    def delete(self, request, pk, format=None):
        item = self.get_object(pk)
        item.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)

class CuisinesSerializer(serializers.ModelSerializer):
    class Meta:
        model = Cuisine
        fields = ('name')