from rest_framework import serializers, status
from rest_framework.response import Response

from .models import *
from django.core.exceptions import ObjectDoesNotExist
from django.forms.models import model_to_dict


class RestaurantSerializer(serializers.ModelSerializer):

    cuisines = serializers.SlugRelatedField(many=True,
                                            slug_field='name',
                                            queryset=Cuisine.objects.all())

    class Meta:
        model = Restaurant
        fields = ('id', 'name', 'city', 'latitude', 'longitude', 'rating', 'is_open', 'cuisines')

    def to_internal_value(self, data):

        l = []
        for c in data['cuisines']:
            try:
                cuis = Cuisine.objects.get(name=c)
                print(cuis.name)
                l.append(cuis)
            except ObjectDoesNotExist:
                cuis = Cuisine(name=c)
                cuis.save()
                cuis = Cuisine.objects.get(name=c)
                l.append(cuis.name)

        data['cuisines'] = l

        return data

    # def create(self, validated_data):
    #
    #     print("validated Data", validated_data)
    #     r = Restaurant(name=validated_data['name'],
    #                    city=validated_data['city'],
    #                    latitude=validated_data['latitude'],
    #                    longitude=validated_data['longitude'],
    #                    rating=validated_data['rating'],
    #                    is_open=validated_data['is_open'])
    #
    #     r.save()
    #
    #     cuisines_data = validated_data.pop('cuisines')
    #
    #     for c in cuisines_data:
    #         ins = Cuisines.objects.create(name=c.name)
    #         ins.restaurant_id.add(r)
    #         ins.save()
    #
    #     return r
    #
    # def update(self, instance, validated_data):
    #     instance.name = validated_data.get('name', instance.name)
    #     instance.city = validated_data.get('city', instance.city)
    #     instance.latitude = validated_data.get('latitude', instance.latitude)
    #     instance.longitude = validated_data.get('longitude', instance.longitude)
    #     instance.rating = validated_data.get('rating', instance.rating)
    #     instance.is_open = validated_data.get('is_open', instance.is_open)

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
        c = [cuis.name for cuis in Cuisine.objects.filter(restaurant_id=instance.restaurant_id)]
        restaurant['cuisines'] = c

        return {
            "id": instance.id,
            "name": instance.name,
            "price": str(float(instance.price)),
            "restaurant": restaurant
        }

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