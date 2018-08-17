FROM gradle

USER root
RUN curl https://bootstrap.pypa.io/get-pip.py -o get-pip.py && python get-pip.py && pip install awscli

ADD . /assignment04
WORKDIR /assignment04

CMD ["sh", "run.sh"]