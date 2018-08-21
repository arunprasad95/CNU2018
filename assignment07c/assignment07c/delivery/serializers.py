from rest_framework import serializers

from .models import Restaurant, Item, Cuisine


class CuisineSerializer(serializers.ModelSerializer):
    class Meta:
        model = Cuisine
        fields = ('name',)


class CuisineField(serializers.Field):
    def to_representation(self, cuisines):
        return list(str(cuisine) for cuisine in cuisines.all())

    def to_internal_value(self, data):
        cuisine_list = [str(cuisine) for cuisine in Cuisine.objects.all()]
        non_existing_cuisines = list(set(data) - set(cuisine_list))
        for cuisine in non_existing_cuisines:
            new_cuisine_object = Cuisine(name=cuisine)
            new_cuisine_object.save()
        result = list()
        for item in data:
            for cuisine_object in Cuisine.objects.all():
                if cuisine_object.name == item:
                    result.append(cuisine_object.id)
        return result


class RestaurantSerializer(serializers.ModelSerializer):
    cuisines = CuisineField()

    class Meta:
        model = Restaurant
        fields = ('id', 'name', 'cuisines', 'city', 'latitude', 'longitude', 'rating', 'is_open')


class ItemSerializer(serializers.ModelSerializer):
    restaurant_id = serializers.PrimaryKeyRelatedField(queryset=Restaurant.objects.all(), source='restaurant')
    restaurant = RestaurantSerializer(read_only=True)
    class Meta:
        model = Item
        fields = ('id', 'name', 'price', 'restaurant_id', 'restaurant')
