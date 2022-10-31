self.addEventListener('push', event => {
  const { title, message } = event.data.json();

  const options = {
    body: message,
  };

  event.waitUntil(self.registration.showNotification(title, options));
});
