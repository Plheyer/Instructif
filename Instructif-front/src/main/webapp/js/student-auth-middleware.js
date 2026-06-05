async function getMe() {
    const todo = "me-student";
    const url = `ActionServlet?todo=${todo}`;
    try {
        const response = await fetch(url);
        return await response.json();
    } catch (error) {
        console.log(error);
        return null;
    }
}

async function initPage() {
    const me = await getMe();
    if (!me) {
        console.error("Not logged in.");
        window.location.href = 'index.html';
    }
}

window.addEventListener("load", initPage);
