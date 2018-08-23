from django.core.exceptions import ObjectDoesNotExist
from rest_framework import serializers, status
from .models import  *
from django.forms import model_to_dict
from rest_framework.fields import ListField
from django.core.exceptions import SuspiciousOperation

def create_cuisne(cname):
    try:
        COBJ = Cuisine.objects.get(name=cname)
    except ObjectDoesNotExist:
        COBJ = Cuisine(name=cname)
        COBJ.save()
    return Cuisine.objects.get(name=cname)
class CuisineSerializer(serializers.ModelSerializer):
    class Meta:
        model = Cuisine
        fields = ('name')

# Serializers define the API representation.
class RestaurantSerializer(serializers.ModelSerializer):
    cuisines = serializers.SlugRelatedField(many=True, slug_field="name", queryset=Cuisine.objects.all())

    def validate(self, data):
        absent_fields = set(self.fields.keys()) - set(self.initial_data.keys()) - set(['id'])
        if absent_fields:
            raise serializers.ValidationError("Fields absent: {}".format(absent_fields))
        latitude = data['latitude']
        longitude = data['longitude']
        rating = data['rating']
        if not (0 <= rating < 100):
            raise serializers.ValidationError('Rating')
        if not (-90 <= latitude < 90):
            raise serializers.ValidationError('latitude ')
        if not (-180 <= longitude < 180):
            raise serializers.ValidationError('longitude')
        return data

    def to_internal_value(self, data):
        data['cuisines'] = map(lambda x: create_cuisne(x), data['cuisines'])
        return data

    class Meta:
        model = Restaurant
        fields = ('id', 'name', 'city', 'latitude', 'cuisines', 'longitude', 'rating', 'is_open')

class ItemSerializer(serializers.ModelSerializer):
    restaurant_id = serializers.PrimaryKeyRelatedField(queryset=Restaurant.objects.all())

    def to_representation(self, instance):
        restaurant = model_to_dict(instance.restaurant_id)
        restaurant['cuisines'] = map(lambda x: x.name, Cuisine.objects.filter(restaurant_id=instance.restaurant_id))
        temp = {}
        temp["id"] = instance.id
        temp["name"] = instance.name
        temp["price"] = "$" + str(float(instance.price))
        temp["restaurant"] = restaurant
        return temp

    def validate(self, data):
        absent_fields = set(self.fields.keys()) - set(self.initial_data.keys()) - set(['id'])
        if absent_fields:
            raise serializers.ValidationError("Fields absent: {}".format(absent_fields))
        price = data['price']
        if not (0 < price):
            raise serializers.ValidationError('Price should be positive')
        return data

    def delete(self, request, pk, format=None):
        item = self.get_object(pk)
        item.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)

    class Meta:
        model = Item
        fields = "__all__"

class UserSerializer(serializers.ModelSerializer):
    name = serializers.CharField(source="username")
    def to_representation(self, instance):
        temp_map = {}
        temp_map["id"] = instance.profile.id
        temp_map["name"] = instance.username
        temp_map["email"] = instance.email
        return temp_map

    class Meta:
        model = User
        fields = ('name', 'email', 'password')

class CartSerializer(serializers.ModelSerializer):
    item_id = serializers.PrimaryKeyRelatedField(queryset=Item.objects.all())

    def to_representation(self, instance):
        item = model_to_dict(instance.item_id)
        item_name = item["name"]
        item_price = '$' + str(float(item["price"]))
        temp_map = {}
        temp_map["item"]={}
        temp_map["item"]["name"] = item_name
        temp_map["item"]["price"] = item_price
        temp_map["quantity"]=instance.quantity
        return temp_map

    def validate(self, data):
        quantity = data['quantity']
        if quantity < 0:
            raise serializers.ValidationError('Quantity should be positive')
        return data

    def to_internal_value(self, data):
        post = data.copy()
        post['order'] = Order.objects.get(pk=1)
        return post

    class Meta:
        model = Cart
        fields = ('item_id', 'quantity')

class OrderSerializer(serializers.ModelSerializer):
    items = CartSerializer(many=True, read_only=True)
    def to_representation(self, instance):
        data = super(OrderSerializer, self).to_representation(instance)
        data["total_price"] = '$' + str(float(instance.total_price))
        return data

    class Meta:
        model = Order
        fields = ('total_price', 'items')
        read_only_fields = ('total_price', )
