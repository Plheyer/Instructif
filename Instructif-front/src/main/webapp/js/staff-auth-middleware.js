import {getMeStaff} from "./auth-middleware-helper";

async function init() {
    const me = await getMeStaff();
    if (!me) {
        console.error("Not logged in.");
        window.location.href = 'index.html';
    }
}

window.addEventListener("load", init);
