"use strict"

const config = {
        zkHost : 'localhost:2181',
        topic: "Topic1"
      },
      kafka = require("kafka-node"),
      KeyedMessage = kafka.KeyedMessage,
      Client = kafka.Client,
      client = new Client(config.zkHost)
;

function startConsumer(){
    const ConsumerGroup = kafka.ConsumerGroup,
          consumerOptions = {
            host: config.zkHost,
            groupId: "NodeTestGroup",
            sessionTimeout: 15000,
            protocol: ['roundrobin'],
            fromOffset: 'earliest'
          },
          //consumerGroup = new ConsumerGroup({id: "NodeClient"}, consumerOptions, ["Topic1"]),
          consumerGroup = new ConsumerGroup(consumerOptions, [config.topic])
    ;

    consumerGroup.on('error', err => {
        console.log("Error on consumer: ", err)
    })

    consumerGroup.on('message',function(message){
        console.log(`${this.client.clientId} read msg [Topic: "${message.topic}", Partition: ${message.partition}, Offset:${message.offset}, value: ${message.value}]`);
    })
}

function startProducer(){
    const Producer = kafka.Producer,
          producer = new Producer(client, { requireAcks: 1 })
    ;
    producer.on('ready', function () {
        const message = 'a message'; //message 1
        const keyedMessage = new KeyedMessage('keyed', 'a keyed message'); //message 2

        producer.send([{ 
            topic: config.topic, 
            messages: [message, keyedMessage], 
        }], function (err, result) {
            console.log('Started, produced 2 messages: ',JSON.stringify(err || result));
        });
    });

    producer.on('error', err => {
        console.log('error on producer: ', err)
    })
}

startProducer()
startConsumer()