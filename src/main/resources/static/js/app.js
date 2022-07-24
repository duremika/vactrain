const socket = new SockJS('/ws');
const stompClient = Stomp.over(socket);
let thisUser;

stompClient.connect({}, onConnected);

async function onConnected(frame) {
    thisUser = frame.headers['user-name'];
    stompClient.subscribe('/chatroom/general', onPublicMessageReceived);
    stompClient.subscribe('/user/' + thisUser + '/private', onPrivateMessageReceived);
    await receiveOldMessages();
    stompClient.send("/chat/message",
        {},
        JSON.stringify({sender: thisUser, messageText: 'I`m joined to chat', status: 'JOIN'}))
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
    if (receiver === '') {
        stompClient.send("/chat/message", {},
            JSON.stringify({sender: thisUser, messageText: text, status: 'MESSAGE'}));
    } else {
        const payloadData = {sender: thisUser, receiver: receiver, messageText: text, status: 'MESSAGE'};
        stompClient.send("/chat/private", {},
            JSON.stringify(payloadData));
        showMessage(payloadData);
    }
    text_form.value = '';
}

function showMessage(payloadData) {
    const text = document.createTextNode(
        payloadData.sender +
        (payloadData.receiver ?
            " [private" +
                (payloadData.receiver === thisUser ?
                "" :
                " to " + payloadData.receiver) +
            "]: " :
            ": "
        ) + payloadData.messageText
    );
    const p = document.createElement('div');
    p.appendChild(text);
    document.getElementById('response')
        .appendChild(p)
}

async function receiveOldMessages() {
    await fetch("/messages")
        .then(value => value.json())
        .then(msgList => msgList.forEach(
            msg => showMessage(msg)
        ))
}