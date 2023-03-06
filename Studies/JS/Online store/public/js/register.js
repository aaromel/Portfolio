const submit = document.getElementById("btnRegister");
const form = document.getElementById("register-form");

// Own code ->
const registerUser = async(data) => {
    try {
        const user = await postOrPutJSON('/api/register', 'POST', {data} );
        return createNotification(`Registered succesfully`, 'notifications-container');
      } catch (error) {
        console.error(error);
        return createNotification('Register failed!', 'notifications-container', false);
      }
}

submit.addEventListener("click", async function(event) {
    event.preventDefault();
    const data = new FormData(form);
    const pw1 = data.get("password")
    const pw2 = data.get("passwordConfirmation")
    if (pw1 != pw2) {
        createNotification("Passwords don't match!", "notifications-container", false);
    }
    else {
        let user = {
            name: data.get("name"),
            email: data.get("email"),
            password: pw1,
        }
        
        try {
            await postOrPutJSON('/api/register', 'POST', user );
            form.reset();
            return createNotification(`Registered succesfully`, 'notifications-container');
          } catch (error) {
            return createNotification('Register failed!', 'notifications-container', false);
          }
    }
});
// <- Own code