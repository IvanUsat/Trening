/*слушатель для проверки текущей информации по своему заказу на ремонт*/
document.getElementById('checkStatusRepairOrder').addEventListener("click", getRepairOrder)

/**
 * Функция для проверки текущей информации по своему заказу на ремонт и отрисовки тела ответа
 */
function getRepairOrder() {
    let id = document.getElementById('idCheck').value;
    let telephoneNumber = document.getElementById('telCheck').value;
    fetch('/api/checkStatus', {
        method: 'POST',
        headers: {
            'Accept': 'application/json',
            'Content-type': 'application/json; charset=UTF-8'
        },
        body: JSON.stringify({
            id: id,
            telephoneNumber: telephoneNumber
        })
    }).then(function (res) {
        if (res.status === 200) {
            res.json().then((repairOrder) => {
                document.getElementById('modal-body_checkStatusRepairOrder').innerHTML = `
                        <table class="table table-bordered">
                            <tr>
                                <td>Номер заказа</td>
                                <td>${repairOrder.id}</td>
                            </tr>

                            <tr>
                                <td>Имя клиента</td>
                                <td>${repairOrder.fullNameClient}</td>
                            </tr>

                            <tr>
                                <td>Номер телефона клиента</td>
                                <td>${repairOrder.telephoneNumber}</td>
                            </tr>

                            <tr>
                                <td>Наименование устройства</td>
                                <td>${repairOrder.nameDevice}</td>
                            </tr>

                            <tr>
                                <td>Описание проблемы</td>
                                <td>${repairOrder.fullTextProblem}</td>
                            </tr>

                            <tr>
                                <td>Дата приема заказа</td>
                                <td>${repairOrder.acceptanceDate}</td>
                            </tr>

                            <tr>
                                <td>Текущий статус заказа</td>
                                <td>${repairOrder.repairOrderType}</td>
                            </tr>
                        </table>`;
            })
        } else {
            document.getElementById('modal-body_checkStatusRepairOrder').innerHTML = `
                    <h5>С такими данными заказа нет.</h5>`;
        }
    })
}