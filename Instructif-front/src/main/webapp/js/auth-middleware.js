import {getMeStaff} from "./auth-middleware-helper.js";
import {getMeStudent} from "./auth-middleware-helper.js";

async function init() {
    const staff = await getMeStaff();
    const student = await getMeStudent();
    if (!staff && !student) {
        console.error("Not logged in.");
        window.location.href = 'index.html';
    }
}

window.addEventListener("load", init);
