async function logout(key) {
    localStorage.removeItem(key);
    const todo = "logout";
    const url = `ActionServlet?todo=${todo}&key=${key}`;
    try {
        await fetch(url);
    } catch (error) {
        console.log(error);
    }
}

function init() {
    document.getElementById('logoutStudentBtn')?.addEventListener("click", async () => {
        await logout("eleve");
        document.location.href = 'index.html';
    });

    document.getElementById('logoutStaffBtn')?.addEventListener("click", async () => {
        await logout("staff");
        document.location.href = 'index.html';
    });
}

window.addEventListener("load", init);