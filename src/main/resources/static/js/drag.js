const panel = document.getElementById("calendarPanel");

// 初期位置（保存データがあれば反映）
try {
  const layout = JSON.parse(savedLayout);
  if (layout.calendar) {
    panel.style.left = layout.calendar.x + "px";
    panel.style.top  = layout.calendar.y + "px";
  }
} catch (e) {
	console.warn("layout parse error", e);
}

let offsetX = 0;
let offsetY = 0;
let isDragging = false;

panel.addEventListener("mousedown", (e) => {
  isDragging = true;
  offsetX = e.clientX - panel.offsetLeft;
  offsetY = e.clientY - panel.offsetTop;
});

document.addEventListener("mousemove", (e) => {
  if (!isDragging) return;
  panel.style.left = (e.clientX - offsetX) + "px";
  panel.style.top  = (e.clientY - offsetY) + "px";
});

document.addEventListener("mouseup", () => {
  isDragging = false;
});

document.querySelector("form").addEventListener("submit", () => {
  const layout = {
    calendar: {
      x: panel.offsetLeft,
      y: panel.offsetTop,
	  width: panel.offsetWidth,
	  height: panel.offsetHeight,
      opacity: 1
    }
  };

  document.getElementById("layoutJson").value =
    JSON.stringify(layout);
});

