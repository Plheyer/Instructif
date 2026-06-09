import {getMeStaff} from "./auth-middleware-helper";
import {getMeStudent} from "./auth-middleware-helper";

async function init() {
    const staff = await getMeStaff();
    const student = await getMeStudent();
    console.log(staff)
    console.log(student)
    if (!staff || !student) {
        console.error("Not logged in.");
        window.location.href = 'index.html';
    }
}

window.addEventListener("load", init);
