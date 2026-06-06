export function getStudent() {
    try {
        return JSON.parse(localStorage.getItem("student"));
    } catch {
        return null;
    }
}

export function setStudent(student) {
    try {
        return localStorage.setItem("student", JSON.stringify(student));
    } catch {
        return null;
    }
}
export function getStaff() {
    try {
        return JSON.parse(localStorage.getItem("staff"));
    } catch {
        return null;
    }
}

export function setStaff(staff) {
    try {
        return localStorage.setItem("staff", JSON.stringify(staff));
    } catch {
        return null;
    }
}
