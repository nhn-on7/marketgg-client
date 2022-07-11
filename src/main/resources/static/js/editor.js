const Editor = toastui.Editor;

const editor = new Editor({
    el: document.querySelector('#editor'),
    height: 'auto',
    initialEditType: 'wysiwyg',
    previewStyle: 'vertical',
    placeholder: '내용을 입력해주세요.'
});

const submitButton = document.getElementById('btn-submit');
submitButton.addEventListener('click', (event) => {
    // SEE: https://nhn.github.io/tui.editor/latest/ToastUIEditorCore
    console.log(`editor.getHTML(): ${editor.getHTML()}`);
    console.log(event);
})
