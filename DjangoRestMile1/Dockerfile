FROM python:3.6.1

ADD requirements.txt /assgn7c/requirements.txt
WORKDIR /assgn7c

RUN pip3 install -r requirements.txt

ADD . /assgn7c
RUN echo "yes" | python3 manage.py collectstatic

RUN python3 manage.py migrate

CMD ["/bin/sh", "./run.sh"]
