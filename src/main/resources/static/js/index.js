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

        payloadData.date = new Date(); // only for client
        showMessage(payloadData);
    }
    const messages = document.getElementsByClassName('messages').item(0);
    setInterval(()=>messages.scrollTo(0, messages.scrollHeight), 50);
    text_form.value = '';
}



function showMessage(payloadData) {
    const msg_history = document.getElementsByClassName("msg_history").item(0);
    if (payloadData.sender === thisUser){
        msg_history.appendChild(outgoingMsg(payloadData));
    } else {
        msg_history.appendChild(incomingMsg(payloadData));
    }

}

async function receiveOldMessages() {
    await fetch("/messages")
        .then(value => value.json())
        .then(msgList => msgList.forEach(
            msg => showMessage(msg)
        ))
}