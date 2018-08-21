from django.http import HttpResponse
from django.shortcuts import render
from rest_framework import viewsets
from .models import *
from .serializers import *

# Create your views here.


class RestaurantViewSet(viewsets.ModelViewSet):
    queryset = Restaurant.objects.all()
    serializer_class = RestaurantSerializer


class ItemViewSet(viewsets.ModelViewSet):
    queryset = Item.objects.all()
    serializer_class = ItemSerializer
