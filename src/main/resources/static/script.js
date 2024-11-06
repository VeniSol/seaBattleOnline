const boardSize = 10;
const player1Board = document.getElementById('player1Board');
const player2Board = document.getElementById('player2Board');
let xhr = new XMLHttpRequest();
let lastFieldForFirstPlayer = null;
let lastFieldForSecondPlayer = null;

const url = prompt("Введите url:");
if (url !== null && url !== "") {
    console.log("Введенный url:", url);
} else {
    window.location.replace(url + "/info");
}

function createBoard(boardElement) {
    boardElement.innerHTML = '';
    for (let i = 0; i < boardSize; i++) {
        const row = document.createElement('tr');
        for (let j = 0; j < boardSize; j++) {
            const cell = document.createElement('td');
            if (boardElement !== player1Board) {
                cell.addEventListener('click', () => makeGuess(cell));
            }
            row.appendChild(cell);
        }
        boardElement.appendChild(row);
    }
}

createBoard(player1Board);
createBoard(player2Board);

setInterval(function () {
    getFields();
    checkWin();
    getCurrentTurn()
}, 500);

// Функция для отображения кораблей и попаданий на доске
function generateField(fields, boardElement) {
    for (let i = 0; i < boardSize; i++) {
        for (let j = 0; j < boardSize; j++) {
            const cell = boardElement.rows[i].cells[j];
            cell.classList.remove('hit', 'miss', 'ship');
            if (fields[i][j] === 4) {
                cell.classList.add("hit"); // Попадание
            } else if (fields[i][j] === 1 && boardElement !== player2Board) {
                cell.classList.add('ship'); // Корабль отображается только на доске игрока 1
            } else if (fields[i][j] === 3) {
                cell.classList.add("miss"); // Промах
            }
        }
    }
}

function arraysEqual(arr1, arr2) {
    if (!arr1 || !arr2) return false;
    if (arr1.length !== arr2.length) return false;
    for (let i = 0; i < arr1.length; i++) {
        for (let j = 0; j < arr1[i].length; j++) {
            if (arr1[i][j] !== arr2[i][j]) return false;
        }
    }
    return true;
}

function getFields() {
    let newUrl = url + "/get-fields";
    xhr.open("GET", newUrl, true);
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.responseType = 'json';
    xhr.onload = function () {
        if (xhr.status === 200) {
            let responseObj = xhr.response;
            let fieldForFirstPlayer = responseObj.fieldForFirstPlayer;
            let fieldForSecondPlayer = responseObj.fieldForSecondPlayer;
            if (!arraysEqual(fieldForFirstPlayer, lastFieldForFirstPlayer) ||
                !arraysEqual(fieldForSecondPlayer, lastFieldForSecondPlayer)) {
                generateField(fieldForFirstPlayer, player1Board);
                generateField(fieldForSecondPlayer, player2Board);
                lastFieldForFirstPlayer = fieldForFirstPlayer;
                lastFieldForSecondPlayer = fieldForSecondPlayer;
            }
        } else {
            console.error('Ошибка при получении данных с сервера:', xhr.statusText);
        }
    };

    xhr.onerror = function () {
        console.error('Ошибка сети');
    };
    xhr.send();
}

function newMove(x, y) {
    let newUrl = url + "/new-move";
    xhr.open("POST", newUrl , true);
    xhr.setRequestHeader("Content-type", "application/json");
    let jsonReq = JSON.stringify({"x": x, "y": y});
    xhr.send(jsonReq);
}

function makeGuess(cell) {
    const x = cell.parentNode.rowIndex;
    const y = cell.cellIndex;
    newMove(x, y);
}

// Проверка условия победы
function checkWin() {
    let xhr = new XMLHttpRequest();
    let newUrl = url + "/who-win";
    xhr.open("GET", newUrl, true);
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.responseType = 'json';
    xhr.onload = function () {
        if (xhr.status === 200) {
            let responseObj = xhr.response;
            if (responseObj !== 0) {
                window.location = url+"/info";
            }
        }
    };
    xhr.onerror = function () {
        console.error('Ошибка сети');
    };
    xhr.send();
}
function getCurrentTurn() {
    let nameFromCookie = getCookie("Haruka");
    let xhr = new XMLHttpRequest();
    let newUrl = url + "/get-current-turn";
    xhr.open("GET", newUrl, true);
    xhr.setRequestHeader("Content-type", "application/json");
    xhr.responseType = 'json';

    xhr.onerror = function () {
        console.error('Ошибка сети');
    };

    xhr.onload = function () {
        if (xhr.status === 200) {
            let response = xhr.response;
            let currentTurnElement = document.getElementById("currentTurn");
            currentTurnElement.textContent = "Это!";
            if (response && String(response.playerId) === String(nameFromCookie)) {
                currentTurnElement.textContent = "Это ваш ход!";
            } else {
                currentTurnElement.textContent = "Сейчас ход другого игрока.";
            }
        } else {
            console.error("Ошибка при получении текущего хода.");
        }
    };

    xhr.send();
}

function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return parts.pop().split(';').shift();
}

