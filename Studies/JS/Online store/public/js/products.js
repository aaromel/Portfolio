// Own code ->

const container = document.getElementById("products-container");
const not = document.getElementById("notifications-container");
const tmp = document.getElementById("product-template");

const addToCart = (productId, productName) => {
  try {
    addProductToCart(productId);
    createNotification(`Added ${productName} to cart!`, "notifications-container");
  }
  catch (error) {
    createNotification("Failed to add to cart!", "notifications-container", isSuccess = false);
  }
  
};

(async() => {
  const res = await getJSON('/api/products');
  res.forEach(product => {
    let clone = tmp.content.cloneNode(true);

    let h = clone.querySelectorAll("h3");
    h[0].textContent = product.name;
    h[0].setAttribute('id', `name-${product._id}`);

    let p = clone.querySelectorAll("p");
    p[0].textContent = product.description;
    p[0].setAttribute('id', `description-${product._id}`);
    p[1].textContent = product.price;
    p[1].setAttribute('id', `price-${product._id}`);

    let btn = clone.querySelectorAll("button");
    btn[0].addEventListener('click', () => addToCart(product._id, product.name));
    btn[0].setAttribute('id', `add-to-cart-${product._id}`);

    container.appendChild(clone);
  });
})();

// <- Own code