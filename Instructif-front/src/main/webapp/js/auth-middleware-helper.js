export async function getMeStaff() {
    const todo = "me-staff";
    const url = `ActionServlet?todo=${todo}`;
    try {
        const response = await fetch(url);
        return await response.json();
    } catch (error) {
        console.log(error);
        return null;
    }
}

export async function getMeStudent() {
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