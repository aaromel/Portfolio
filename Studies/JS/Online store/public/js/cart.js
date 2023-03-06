// Own code ->

const container = document.getElementById("cart-container");
const tl = document.getElementById("cart-item-template");
const placeOrderBtn = document.getElementById("place-order-button");
placeOrderBtn.addEventListener('click', () => placeOrder());

const addToCart = productId => {
  addProductToCart(productId);
  updateProductAmount(productId);
};

const decreaseCount = productId => {
  let amt = decreaseProductCount(productId);
  if (amt == 0) {
    removeElement('cart-container', `product-${productId}`);
  }
  else {
    updateProductAmount(productId);
  }
};

const updateProductAmount = productId => {
  let count = getProductCountFromCart(productId);
  const elem = document.getElementById(`amount-${productId}`);
  elem.textContent = `${count}x`;


};

const placeOrder = async() => {
  let all = getAllProductsFromCart();
  createNotification("Order placed!", "notifications-container", isSuccess = true);
  all.forEach(prod => {
    removeElement('cart-container', `product-${prod.id}`);
  });
};

(async() => {
  const res = await getJSON('/api/products');
  let prods = getAllProductsFromCart();
  prods.forEach(prod => {
    let clone = tl.content.cloneNode(true);
    
    let x = res.filter(obj => {
      return obj._id == prod.id;
    })
    x = x[0];

    let h = clone.querySelectorAll("h3");
    h[0].textContent = x.name;
    h[0].setAttribute('id', `name-${prod.id}`);

    let d = clone.querySelectorAll("div");
    d[0].setAttribute('id', `product-${prod.id}`);

    let p = clone.querySelectorAll("p");
    p[0].textContent = x.price;
    p[0].setAttribute('id', `price-${prod.id}`);
    p[1].textContent = `${prod.amount}x`;
    p[1].setAttribute('id', `amount-${prod.id}`);

    let btn = clone.querySelectorAll("button");
    btn[0].addEventListener('click', () => addToCart(prod.id));
    btn[0].setAttribute('id', `plus-${prod.id}`);
    btn[1].addEventListener('click', () => decreaseCount(prod.id));
    btn[1].setAttribute('id', `minus-${prod.id}`);

    container.appendChild(clone);
  });
})();
// <- Own code