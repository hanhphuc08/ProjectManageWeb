(function () {
  // ===== helpers =====
  const qs = (s, r=document) => r.querySelector(s);
  const qsa = (s, r=document) => [...r.querySelectorAll(s)];

  const calRoot = qs("#calendar-content") || document;
  const tasks = () => qsa(".pd-calendar-task", calRoot);

  // ===== state filters =====
  const state = {
    q: "",
    assignee: new Set(), // selected assignees
    type: new Set(),
    status: new Set(),
  };

  function normalize(str){
    return (str || "").toString().trim().toLowerCase();
  }

  function applyFilters(){
    tasks().forEach(task => {
      const title = normalize(task.dataset.title);
      const assignee = normalize(task.dataset.assignee);
      const type = normalize(task.dataset.type);
      const status = normalize(task.dataset.status);

      const okQ = !state.q || title.includes(state.q);
      const okA = state.assignee.size === 0 || state.assignee.has(assignee);
      const okT = state.type.size === 0 || state.type.has(type);
      const okS = state.status.size === 0 || state.status.has(status);

      task.style.display = (okQ && okA && okT && okS) ? "inline-flex" : "none";
    });
  }

  // ===== Search realtime =====
  const searchInput = qs("#pdCalSearch", calRoot);
  searchInput?.addEventListener("input", () => {
    state.q = normalize(searchInput.value);
    applyFilters();
  });

  // ===== Build dropdown options from current tasks =====
  function collectOptions(key){
    const set = new Set();
    tasks().forEach(t => {
      const v = normalize(t.dataset[key]);
      if (v) set.add(v);
    });
    return [...set].sort();
  }

  function renderDropdown(key){
    const dropdown = qs(`.pd-filter-dropdown[data-dropdown="${key}"]`, calRoot);
    if (!dropdown) return;

    const options = collectOptions(key);
    dropdown.innerHTML = "";

    if (options.length === 0){
      dropdown.innerHTML = `<div class="pd-filter-item disabled">No data</div>`;
      return;
    }

    options.forEach(opt => {
      const id = `${key}-${opt.replace(/\s+/g,'-')}`;

      const row = document.createElement("label");
      row.className = "pd-filter-item";
      row.innerHTML = `
        <input type="checkbox" id="${id}" data-key="${key}" value="${opt}">
        <span>${opt}</span>
      `;
      dropdown.appendChild(row);
    });

    // clear button
    const clear = document.createElement("div");
    clear.className = "pd-filter-item";
    clear.style.opacity = "0.8";
    clear.innerHTML = `✕ Clear`;
    clear.addEventListener("click", () => {
      state[key].clear();
      qsa(`input[data-key="${key}"]`, dropdown).forEach(i => i.checked = false);
      applyFilters();
    });
    dropdown.appendChild(clear);
  }

  // render all dropdowns once page loaded
  ["assignee","type","status"].forEach(renderDropdown);

  // ===== Dropdown toggle logic =====
  qsa(".pd-calendar-filter", calRoot).forEach(btn => {
    btn.addEventListener("click", (e) => {
      const key = btn.dataset.filter;
      const dd = qs(`.pd-filter-dropdown[data-dropdown="${key}"]`, calRoot);
      if (!dd) return;

      // close others
      qsa(".pd-filter-dropdown.open", calRoot)
        .forEach(x => { if (x !== dd) x.classList.remove("open"); });

      dd.classList.toggle("open");
      e.stopPropagation();
    });
  });

  // close dropdown when click outside
  document.addEventListener("click", () => {
    qsa(".pd-filter-dropdown.open", calRoot)
      .forEach(x => x.classList.remove("open"));
  });

  // ===== Checkbox change => update state =====
  calRoot.addEventListener("change", (e) => {
    const input = e.target;
    if (!(input instanceof HTMLInputElement)) return;
    if (!input.dataset.key) return;

    const key = input.dataset.key;
    const val = normalize(input.value);

    if (input.checked) state[key].add(val);
    else state[key].delete(val);

    applyFilters();
  });

  // ===== Month navigation (Today/Prev/Next) =====
  const prevBtn = qs("#pdCalPrevBtn", calRoot);
  const nextBtn = qs("#pdCalNextBtn", calRoot);
  const todayBtn = qs("#pdCalTodayBtn", calRoot);

  function gotoMonth(offset){
    const url = new URL(window.location.href);
    const cur = url.searchParams.get("month"); // YYYY-MM
    let [y, m] = cur ? cur.split("-").map(Number)
                    : [new Date().getFullYear(), new Date().getMonth()+1];

    m += offset;
    if (m <= 0) { m = 12; y--; }
    if (m >= 13) { m = 1; y++; }

    url.searchParams.set("month", `${y}-${String(m).padStart(2,"0")}`);
    window.location.href = url.toString();
  }

  prevBtn?.addEventListener("click", () => gotoMonth(-1));
  nextBtn?.addEventListener("click", () => gotoMonth(1));
  todayBtn?.addEventListener("click", () => {
    const now = new Date();
    const url = new URL(window.location.href);
    url.searchParams.set("month",
      `${now.getFullYear()}-${String(now.getMonth()+1).padStart(2,"0")}`);
    window.location.href = url.toString();
  });

})();
