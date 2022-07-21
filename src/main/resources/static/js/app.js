let stompClient = null;

function connect() {
    const socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        stompClient.subscribe('/chatroom/general', function (greeting) {
            showGreeting(JSON.parse(greeting.body).content);
        });
    });
}

function sendName() {
    stompClient.send("/app/message", {}, JSON.stringify({'name': "X"}));
}

function showGreeting(message) {
    $("#main").append("<p>" + message + "</p>");
}

function d () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#send" ).click(function() { sendName(); });
}