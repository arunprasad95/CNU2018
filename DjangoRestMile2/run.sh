gunicorn assgn7c.wsgi:application \
    --bind 0.0.0.0:8080 \
    --workers 3

