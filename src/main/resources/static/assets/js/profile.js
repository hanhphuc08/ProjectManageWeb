(function(){
  function onInput(e){
    const field = e.target.closest('.pf-field');
    if(!field) return;
    const original = field.getAttribute('data-original') || '';
    const current = e.target.value || '';
    if(current !== original){
      field.classList.add('is-dirty');
    } else {
      field.classList.remove('is-dirty');
    }
  }
  function onClick(e){
    const btn = e.target.closest('[data-action]');
    if(!btn) return;
    const action = btn.getAttribute('data-action');
    const field = btn.closest('.pf-field');
    const input = field.querySelector('.pf-input');
    if(action === 'save'){
      field.setAttribute('data-original', input.value || '');
      field.classList.remove('is-dirty');
      // TODO: Hook up API call here if needed
    }
    if(action === 'cancel'){
      input.value = field.getAttribute('data-original') || '';
      field.classList.remove('is-dirty');
    }
  }
  document.querySelectorAll('.pf-field').forEach(f => {
    const input = f.querySelector('.pf-input');
    if(input){
      f.setAttribute('data-original', input.value || '');
      input.addEventListener('input', onInput);
    }
  });
  document.addEventListener('click', onClick);
})();
