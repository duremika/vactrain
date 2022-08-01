const users = document.getElementsByClassName("users").item(0);
const searchInput = document.getElementById('search_input');
let allUsers = [];
searchInput.addEventListener('input', e => {
    allUsers.forEach(usr => updateUserList(usr));
});

function updateUserList(user_obj) {
    const username = user_obj.username;
    const isOnline = username === thisUser ? true : user_obj.isOnline;

    const foundUser = document.getElementById(username)
    if (foundUser) {
        foundUser.className = username.includes(searchInput.value) ? "clearfix" : "clearfix hidden";
        const status = foundUser
            .getElementsByClassName('about').item(0)
            .getElementsByClassName('status').item(0);

        status.innerHTML = '';

        const indicator = document.createElement("i");
        indicator.className = `fa fa-circle ${isOnline ? 'online' : 'offline'}`;

        status.appendChild(indicator);
        status.appendChild(
            document.createTextNode(isOnline ? ' online' : ' offline')
        );

        return;
    }


    const user = document.createElement("li");
    user.className = username.includes(searchInput.value) ? "clearfix" : "clearfix hidden";
    user.id = username;
    user.onclick = () => {selectChat(username);};

    const img = document.createElement("img");
    img.className = "mt-4"
    img.src = random_img();
    img.alt = "avatar";

    const about = document.createElement("div");
    about.className = "about";

    const name = document.createElement("div");
    name.className = "name";
    name.appendChild(
        document.createTextNode(username)
    );

    const status = document.createElement("div");
    status.className = "status";

    const indicator = document.createElement("i");
    indicator.className = `fa fa-circle ${isOnline ? 'online' : 'offline'}`;

    status.appendChild(indicator);
    status.appendChild(
        document.createTextNode(isOnline ? ' online' : ' offline')
    );

    about.appendChild(name);
    about.appendChild(status);

    user.appendChild(img);
    user.appendChild(about);

    users.appendChild(user);
}

const textArray = [
    'https://bootdey.com/img/Content/avatar/avatar1.png',
    'https://bootdey.com/img/Content/avatar/avatar2.png',
    'https://bootdey.com/img/Content/avatar/avatar3.png',
    'https://bootdey.com/img/Content/avatar/avatar4.png',
    'https://bootdey.com/img/Content/avatar/avatar5.png',
    'https://bootdey.com/img/Content/avatar/avatar6.png',
    'https://bootdey.com/img/Content/avatar/avatar7.png',
    'https://bootdey.com/img/Content/avatar/avatar8.png',
];

const random_img = () =>
    textArray[
        Math.floor(Math.random() * textArray.length)
        ];