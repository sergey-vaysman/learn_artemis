# Необходимые настройки #

broker.xml
```
<addresses>
    <address name="exampleQueue">
        <anycast>
            <queue name="exampleQueue"/>
        </anycast>
    </address>
    <address name="exampleTopic">
        <multicast/>
    </address>
</addresses>
```

# Запуск #
Для очереди 
```
$ mvn exec:java -Dexec.args="queue"
```
Для топика
```
$ mvn exec:java -Dexec.args="topic"
```

