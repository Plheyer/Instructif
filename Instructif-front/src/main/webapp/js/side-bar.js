import { getStudent } from "./localStorage-helper.js";
import {loadRecentList} from "./aside-helper.js";

async function init() {
    const student = getStudent();
    if (!student || !student.firstName || !student.lastName || !student.schoolGrade) {
        console.error("Can't load the student")
        return;
    }
    initials(student);
    studentName(student);
    schoolGrade(student);
    await loadRecentList();
}

function initials(student) {
    document.getElementById("initials").innerText = student.firstName[0].toUpperCase() + student.lastName[0].toUpperCase()
}

function studentName(student) {
    document.getElementById("name").innerText = student.firstName + ' ' + student.lastName;
}

function schoolGrade(student) {
    document.getElementById("student-school-grade").innerText = student.schoolGrade + 'ème';
}

window.addEventListener("load", init);