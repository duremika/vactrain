const socket = new SockJS('/ws');
const stompClient = Stomp.over(socket);
let sender;

stompClient.connect({}, onConnected);

function onConnected(frame) {
    sender = frame.headers['user-name'];
    stompClient.subscribe('/chatroom/general', onPublicMessageReceived);
    stompClient.subscribe('/user/' + sender + '/private', onPrivateMessageReceived);
    stompClient.send("/chat/message",
        {},
        JSON.stringify({sender: sender, messageText: 'I`m joined to chat', status: 'JOIN'}))
}

const onPublicMessageReceived = (payload) => {
    const payloadData = JSON.parse(payload.body);
    switch (payloadData.status) {
        case "JOIN":
            showMessage(payloadData);
            break;
        case "LEAVE":
            break;
        case "MESSAGE":
            showMessage(payloadData);
            break;
    }
}

const onPrivateMessageReceived = (payload) => {
    const payloadData = JSON.parse(payload.body);
    payloadData.messageText = '[private] ' + payloadData.messageText;
    switch (payloadData.status) {
        case "JOIN":
            showMessage(payloadData);
            break;
        case "LEAVE":
            break;
        case "MESSAGE":
            showMessage(payloadData);
            break;
    }
}

function sendMessage() {
    const text_form = document.getElementById('text');
    const text = text_form.value;
    const receiver = document.getElementById('receiver').value;
    if (receiver === ''){
        stompClient.send("/chat/message", {},
            JSON.stringify({sender: sender, messageText: text, status: 'MESSAGE'}));
    } else {
        stompClient.send("/chat/private", {},
            JSON.stringify({sender: sender, receiver: receiver, messageText: text, status: 'MESSAGE'}));
        stompClient.send("/chat/private", {},
            JSON.stringify({sender: sender, receiver: sender, messageText: text, status: 'MESSAGE'}));
    }
    text_form.value = '';
}

function showMessage(payloadData) {
    const text = document.createTextNode(
        payloadData.sender + ': ' + payloadData.messageText
    );
    const p = document.createElement('div');
    p.appendChild(text);
    document.getElementById('response')
        .appendChild(p)
}