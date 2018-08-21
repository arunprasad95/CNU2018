from django.http import HttpResponse
from django.shortcuts import render
from rest_framework import viewsets
from django_filters import rest_framework as filters
from .models import *
from .serializers import *
import django_filters
# Create your views here.


class ItemFilter(filters.FilterSet):
    name = filters.CharFilter(lookup_expr='icontains', field_name="name")
    minPrice = filters.NumberFilter(field_name='price', lookup_expr='gte')
    maxPrice = filters.NumberFilter(field_name='price', lookup_expr='lte')
    class Meta:
        model = Item
        fields = ["name", "minPrice", "maxPrice"]


class RestaurantFilter(filters.FilterSet):
    name = filters.CharFilter(lookup_expr='icontains', field_name="name")
    cuisine = filters.ModelMultipleChoiceFilter(field_name='cuisines', queryset=Cuisine.objects.all())
    city = filters.CharFilter(lookup_expr='icontains', field_name="city")

    class Meta:
        model = Restaurant
        fields = ["name", "cuisine", "city"]


class RestaurantViewSet(viewsets.ModelViewSet):
    queryset = Restaurant.objects.all()
    serializer_class = RestaurantSerializer
    filter_backends = (filters.DjangoFilterBackend,)
    filter_class = RestaurantFilter


class ItemViewSet(viewsets.ModelViewSet):
    queryset = Item.objects.all()
    serializer_class = ItemSerializer
    filter_backends = (filters.DjangoFilterBackend,)
    filter_class = ItemFilter
