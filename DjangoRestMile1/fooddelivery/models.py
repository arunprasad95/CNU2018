from django.db import models

# Create your models here.

class Restaurant(models.Model):
    id = models.AutoField(primary_key=True)
    name = models.CharField(max_length=30)
    city = models.CharField(max_length=30)
    latitude = models.DecimalField(max_digits=20, decimal_places=10, default=None)
    longitude = models.DecimalField(max_digits=20, decimal_places=10, default=None)
    rating = models.DecimalField(max_digits=20, decimal_places=10, default=None)
    is_open = models.BooleanField(default=True)
    def __str__(self):
        return self.name
class Cuisine(models.Model):
    name = models.CharField(max_length=30, primary_key=True)
    restaurant_id = models.ManyToManyField('Restaurant', related_name="cuisines")

    def __unicode__(self):
        return self.name
class Item(models.Model):
    id = models.AutoField(primary_key=True)
    name = models.CharField(max_length=30)
    price = models.DecimalField(max_digits=20, decimal_places=10)
    restaurant_id = models.ForeignKey('Restaurant', on_delete=models.CASCADE)

    def __str__(self):
        return self.name