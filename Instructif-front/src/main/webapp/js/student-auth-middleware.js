import {getMeStudent} from "./auth-middleware-helper.js";

async function init() {
    const me = await getMeStudent();
    if (!me) {
        console.error("Not logged in.");
        window.location.href = 'index.html';
    }
}

window.addEventListener("load", init);
