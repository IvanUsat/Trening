/**
 * Declaration of global variables
 */
let myHeaders = new Headers()
let newsApiUrl = "/api/manager/news"
myHeaders.append('Content-type', 'application/json; charset=UTF-8')
const lastPage = {type: 'ALL', currentDate: new Date().toLocaleDateString(), divId: '#allNews', number: 0, last: false};
let clickUpperNavButton = 'allNews'

$(document).ready(function () {
    fetchNews();

    /**
     * Event listeners of all document
     */
    /*event listener in news div of edit and delete buttons*/
    document.getElementById('newsTabContent').addEventListener('click', checkButton)
    /*modal window publish checkbox listener*/
    document.getElementById('archiveCheckboxDiv').addEventListener('change', archiveCheckboxHandler)
    /*modal window editSave */
    document.getElementById('editSave').addEventListener('click', checkButtonClicked)
    /*left nav bar*/
    document.getElementById('leftNavBar').addEventListener('click', handleLeftNavBarClick)
    /*upper nav tab*/
    document.getElementById('upperNavTab').addEventListener('click', checkUpperNavButton)

    $(window).scroll(function () {
        yHandler('allNews');
    });
});


function yHandler(divId) {
    if (lastPage.last) {
        return;
    }
    let allNews = document.getElementById(divId);
    let contentHeight = allNews.offsetHeight;
    let yOffset = window.pageYOffset;
    let y = yOffset + window.innerHeight;
    if (y >= contentHeight) {
        fetchNews();
    }
}

/**
 * Function checks which button on top nav bar was clicked
 * @param event
 */
function checkUpperNavButton(event) {
    $(window).unbind('scroll');
    let tab = event.target.dataset.toggleId
    lastPage.number = 0
    lastPage.last = false
    lastPage.currentDate = new Date().toLocaleDateString();
    if (tab === 'publishedNews') {
        clickUpperNavButton = tab
        initFetchNews('PUBLISHED', tab);
    }
    if (tab === 'allNews') {
        clickUpperNavButton = tab
        initFetchNews('ALL', tab);
    }
    if (tab === 'unpublishedNews') {
        clickUpperNavButton = tab
        initFetchNews('UNPUBLISHED', tab);
    }
    if (tab === 'archivedNews') {
        clickUpperNavButton = tab
        initFetchNews('ARCHIVED', tab);
    }
}

/**
 * Function checks which button on top nav bar was clicked
 * and click suitable nav bar
 */
function checkAndClickUpperNavButton() {
    if (clickUpperNavButton === 'publishedNews') {
        document.getElementById('nav-link_publishedNews').click()
    }
    if (clickUpperNavButton === 'allNews') {
        document.getElementById('nav-link_allNews').click()
    }
    if (clickUpperNavButton === 'unpublishedNews') {
        document.getElementById('nav-link_unpublishedNews').click()
    }
    if (clickUpperNavButton === 'archivedNews') {
        document.getElementById('nav-link_archivedNews').click()
    }
}

function initFetchNews(type, divId) {
    lastPage.type = type;
    lastPage.divId = '#' + divId;
    fetchNews();
    $(window).scroll(function () {
        yHandler(divId);
    })
}

/**
 * function check which button was clicked in news dev (edit or delete)
 * @param event - event from button
 */
function checkButton(event) {
    let newsId = event.target.dataset.newsId
    if (event.target.dataset.toggleId === 'edit') {
        renderEditModalWindow(newsId)
    }
    if (event.target.dataset.toggleId === 'delete') {
        handleDeleteButton(newsId)
    }
}

/**
 * checks which button was clicked in modal window update or add
 * @param event
 */
function checkButtonClicked(event) {
    if (event.target.dataset.toggleId === 'update') {
        updateNews(event.target.dataset.newsId)
    }
    if (event.target.dataset.toggleId === 'add') {
        addNewNews()
    }
}

/**
 * function makes POST request to add news to news api
 */
function addNewNews() {
    let newNews = {
        title: document.getElementById('titleNewsUpdate').value,
        anons: document.getElementById('anonsNewsUpdate').value,
        fullText: document.getElementById('fullTextUpdate').value,
        postingDate: moment(document.getElementById('postingDateUpdate').value).format("yyyy-MM-DD"),
        archived: document.getElementById('archiveCheckbox').checked
    }
    if (checkFields()) {
        fetch(newsApiUrl, {
            method: 'POST',
            headers: myHeaders,
            body: JSON.stringify(newNews)
        }).then(function (response) {
            if (response.status === 200) {
                infoMessage('#infoMessageMainPage', '?????????????? ?????????????? ??????????????????', 'success')
            } else {
                infoMessage('#infoMessageMainPage', '?????????????? ???? ??????????????????', 'alert')
            }
        }).then(() => {
            $('#editNewsModal').modal('hide')
            checkAndClickUpperNavButton()
        })
    }
}

/**
 * function makes PUT request to update news
 */
function updateNews() {
    let news = {
        id: document.getElementById('idNewsUpdate').value,
        title: document.getElementById('titleNewsUpdate').value,
        anons: document.getElementById('anonsNewsUpdate').value,
        fullText: document.getElementById('fullTextUpdate').value,
        postingDate: document.getElementById('postingDateUpdate').value,
        archived: document.getElementById('archiveCheckbox').checked
    }
    if (checkFields()) {
        fetch(newsApiUrl, {
            method: 'PUT',
            headers: myHeaders,
            body: JSON.stringify(news)
        }).then(function (response) {
            if (response.status === 200) {
                infoMessage('#infoMessageMainPage', '?????????????? ?????????????? ??????????????????', 'success')
            } else {
                infoMessage('#infoMessageMainPage', '?????????????? ???? ??????????????', 'error')
            }
        }).then(() => {
            $('#editNewsModal').modal('hide')
            checkAndClickUpperNavButton()
        })
    }
}

/**
 * function checks which button was pressed in left nav bar
 * @param event
 */
function handleLeftNavBarClick(event) {
    if (event.target.dataset.toggleId === 'addNewNews') {
        renderNewNewsModal()
    }
}

/**
 * function handles $('#addNewNews') ("???????????????? ??????????????") button click in left nav bar
 */
function renderNewNewsModal() {
    $('form[name=editNewsFormModal]').trigger('reset')
    $('#idModalDiv').hide()
    $('#modalWindowTitle').text('???????????????? ??????????????')
    $('#postingDateUpdate').attr('disabled', false)
    $('#editSave').text('????????????????')
    $('#editSave').attr('data-toggle-id', "add")
    $('#postingDateUpdate').attr('min', moment(new Date).format("yyyy-MM-DD"))
    $('#archiveCheckboxDiv').hide()
    $('#fullTextUpdate').summernote('code', '')
    document.getElementById('titleNewsUpdate').value = ''
    document.getElementById('anonsNewsUpdate').value = ''
    document.getElementById('postingDateUpdate').value = ''
}

/**
 * Renders edit model windows
 * @param newsId - Long id of news to edit
 */
function renderEditModalWindow(newsId) {
    $('#editSave').text('????????????????')
    $('#editSave').attr('data-toggle-id', "update")
    $('#editSave').attr('data-news-id', newsId)
    $('#postingDateUpdate').attr('min', moment(new Date).format("yyyy-MM-DD"))
    $('#archiveCheckboxDiv').show()
    fetch(newsApiUrl + `/${newsId}`, {
        headers: myHeaders
    }).then(function (response) {
        if (response.status === 200) {
            response.json().then(news => renderEditModal(news))
        } else {
            console.log('news not found')
            infoMessage('#infoMessageMainPage', '?????????????? ???? ??????????????', 'error')
        }
    })

    function renderEditModal(news) {
        let now = new Date()
        $('#idModalDiv').show()
        $('#modalWindowTitle').text('?????????????????????????? ??????????????')
        $('form[name=editNewsFormModal]').trigger('reset')
        if (moment(news.postingDate).isBefore(now, 'day')) {
            $('#postingDateUpdate').attr('disabled', true)
        } else {
            $('#postingDateUpdate').attr('disabled', false)
        }
        $('#idNewsUpdate').val(news.id)
        $('#titleNewsUpdate').val(news.title)
        $('#anonsNewsUpdate').val(news.anons)
        $('#fullTextUpdate').summernote('code', news.fullText)
        $("#postingDateUpdate").val(moment(news.postingDate).format("yyyy-MM-DD"))
        $('#archiveCheckbox').prop('checked', news.archived)
    }
}

/**
 * function check modal window fields to be filled
 * @returns {boolean}
 */
function checkFields() {
    if (document.getElementById('titleNewsUpdate').value === '') {
        invalidModalField('?????????????? ?????????????????? ??????????????!', document.getElementById('titleNewsUpdate'))
        return false
    } else if (document.getElementById('anonsNewsUpdate').value === '') {
        invalidModalField('?????????????? ?????????? ??????????????!', document.getElementById('anonsNewsUpdate'))
        return false
    } else if (document.getElementById('fullTextUpdate').value === '') {
        invalidModalField('?????????????? ??????????????!', document.getElementById('fullTextUpdate'))
        return false
    } else if (document.getElementById('postingDateUpdate').value === '') {
        invalidModalField('?????????????? ???????? ????????????????????!', document.getElementById('postingDateUpdate'))
        return false
    }
    return true
}

/**
 * function handles delete button
 * @param newsId - Long id of news to delete
 */
function handleDeleteButton(newsId) {
    let doDelete = confirm(`???? ?????????????? ?????? ???????????? ?????????????? ???????????????`);
    if (doDelete) {
        fetch(newsApiUrl + `/${newsId}`, {
            method: 'DELETE',
            headers: myHeaders
        }).then(function (response) {
            if (response.status === 200) {
                $('#div-' + newsId).remove()
                infoMessage('#infoMessageMainPage', '?????????????? ?????????????? ??????????????', 'success')
            } else {
                console.log('news not found')
                infoMessage('#infoMessageMainPage', '?????????????? ???? ??????????????', 'error')
            }
        }).finally(() => {
            checkAndClickUpperNavButton()
        })
    }
}

/**
 * function that handles publish checkbox
 */
function archiveCheckboxHandler() {
    if (document.getElementById('archiveCheckbox').checked) {
        document.getElementById('archiveCheckboxLabel').innerHTML = '???????????? ?????????????? ???? ?????????????'
    } else {
        document.getElementById('archiveCheckboxLabel').innerHTML = '?????????????????? ?????????????? ?? ???????????'
    }
}

/**
 * makes fetch request to manager rest controller
 */
function fetchNews() {
    $.ajax(newsApiUrl + '/page', {
        headers: myHeaders,
        data: {page: lastPage.number, sort: 'postingDate,DESC', type: lastPage.type, currentDate: lastPage.currentDate},
        async: false,
        success: function (data) {
            lastPage.number = data.number + 1;
            lastPage.last = data.last;
            renderNewsTable(data)
        },
        error: function () {
            infoMessage('#infoMessageMainPage', '?? ???????? ?????????????? ?????? ????????????????', 'error')
        }
    })
}

/**
 * function renders news page
 * @param news Page of news
 */
function renderNewsTable(news) {
    const allNewsUl = $(lastPage.divId);
    if (news.number === 0) {
        allNewsUl.empty()
    }
    news.content.forEach(function (element) {
        let postingDate = moment(element.postingDate).format("yyyy-MM-DD")
        let modifiedDateText = ''
        if (element.modifiedDate !== null) {
            modifiedDateText = `???????? ???????????????????? ??????????????????: ` + moment(element.modifiedDate).format("yyyy-MM-DD")
        }
        let row = `<div id="div-${element.id}" class="alert alert-info mt-2">
                        <h2 id="title">${element.title}</h2>
                        <h5 id="anons">${element.anons}</h5>
                        <p id="fullText">${element.fullText}</p>
                        <div class="container-fluid row" id="divContainer">
                            <div class="text-left align-text-bottom col" id="divSpan">
                                <span id="postingDate">???????? ????????????????????: ${postingDate}</span>
                                <br>
                                <span id="modifiedDate">${modifiedDateText}</span>
                            </div>
                            <div class="text-right col" id="divButtons">
                                <button type="button" data-toggle="modal" class="btn btn-primary"  id="btn_edit_news" data-target="#editNewsModal" data-toggle-id="edit" data-news-id="${element.id}">??????????????????????????</button>
                                <button type="button" data-toggle="modal" class="btn btn-danger"  id="btn_delete_news" data-toggle-id="delete" data-news-id="${element.id}" >??????????????</button>
                            </div>
                        </div>
                    </div>`
        allNewsUl.append(row)
    })
}

/**
 * function that shows success or error message
 * @param inputField - location where message appears
 * @param text - text of message
 * @param messageStatus - status (String): 'success' or 'error'
 */
function infoMessage(inputField, text, messageStatus) {
    let successMessage = `<div class="alert text-center alert-success alert-dismissible info-message" role="alert">
                            <strong>${text}</strong>
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                            <span aria-hidden="true">&times;</span></button>
                       </div>`
    let alertMessage = `<div class="alert text-center alert-danger alert-dismissible" role="alert">
                            <strong>${text}</strong>
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                            <span aria-hidden="true">&times;</span></button>
                       </div>`
    let message = ''

    if (messageStatus === "success") {
        message = successMessage
    } else if (message === "error") {
        message = alertMessage;
    }
    window.scroll(0, 0)

    document.querySelector(`${inputField}`).innerHTML = message
    window.setTimeout(function () {
        $('.info-message').alert('close');
    }, 3000)
}

/**
 * Function creates allert message when fields in modal are invalid
 * @param text - text of message
 * @param focusField - field to focus
 */
function invalidModalField(text, focusField) {
    document.querySelector('#modal-alert').innerHTML = `<div class="alert alert-danger alert-dismissible fade show" role="alert">
                                                                    <strong>${text}</strong>
                                                                     <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                                                         <span aria-hidden="true">&times;</span>
                                                                     </button>
                                                                </div>`
    focusField.focus()
    window.setTimeout(function () {
        $('.alert').alert('close')
    }, 3000)
}



