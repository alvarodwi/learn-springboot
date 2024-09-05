from hashicorp/consul:1.19.2
copy ./consul-kv-docker.json /consul
workdir /consul
env CONSUL_HTTP_ADDR=consul:8500
entrypoint until consul kv import @consul-kv-docker.json; do echo "Waiting for consul"; sleep 2; done