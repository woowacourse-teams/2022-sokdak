const scrollToCurrent = (current: number) => {
  document.body.style.height = document.body.scrollHeight + current + 'px';

  window.scrollTo({
    top: document.documentElement.scrollTop + current,
    behavior: 'smooth',
  });

  document.body.style.height = document.body.scrollHeight - current + 'px';
};

export default scrollToCurrent;
