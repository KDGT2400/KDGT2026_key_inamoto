function applyTheme(theme) {
  document.body.classList.remove(
    'theme-light',
    'theme-dark',
    'theme-blue'
  );
  document.body.classList.add(`theme-${theme}`);
}
