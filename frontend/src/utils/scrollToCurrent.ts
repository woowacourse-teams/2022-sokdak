const scrollToCurrent = (current: number) => {
  window.scrollTo({
    top: document.documentElement.scrollTop + current,
    behavior: 'smooth',
  });
};

export default scrollToCurrent;
