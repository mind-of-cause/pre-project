document.addEventListener("DOMContentLoaded", function () {
    fetch('/api/users/current')
        .then(response => {
            if (!response.ok) {
                throw new Error('Сетевой ответ был не ok.');
            }
            return response.json();
        })
        .then(userData => {
            const isAdmin = userData.roles.some(role => role.name === 'ROLE_ADMIN');

            const {id, username, email, roles} = userData;

            document.getElementById('currentUserId').textContent = id;
            document.getElementById('currentUserUsername').textContent = username;
            document.getElementById('currentUserEmail').textContent = email;

            const rolesDiv = document.getElementById('currentUserRoles');
            rolesDiv.innerHTML = roles.map(role => role.name.replace('ROLE_', '')).join(', ');

            if (!isAdmin) {
                document.getElementById('adminContent').style.display = 'none';
            }
        })
        .catch(error => {
            console.error('Возникла проблема с вашим fetch запросом:', error);
        });

    // Функция для получения текущего пользователя и его ролей
    function fetchAndUpdateCurrentUser() {
        fetch('/api/users/current')
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok.');
                }
                return response.json();
            })
            .then(currentUser => {
                console.log(currentUser);  // Добавлено для дебага
                const navbarUsername = document.getElementById('navbarUsername');
                const navbarRoles = document.getElementById('navbarRoles');
                const navbarRolesPrefix = document.getElementById('navbarRolesPrefix');

                if (navbarUsername && currentUser.username) {  // Проверяем наличие элемента и данных
                    navbarUsername.textContent = currentUser.username;
                }

                if (navbarRoles && currentUser.roles) {  // Проверяем наличие элемента и данных
                    const rolesFormatted = currentUser.roles.map(role => role.name.replace('ROLE_', '')).join(', ');
                    navbarRoles.textContent = rolesFormatted;
                    navbarRolesPrefix.style.display = currentUser.roles.length > 0 ? 'inline' : 'none';
                }
            })
            .catch(error => {
                console.error('Error while fetching current user:', error);
            });


    }

    var adminContent = document.getElementById('adminContent');


    var userInfo = document.getElementById('userInfo');

    function clearModal() {
        document.querySelectorAll('.modal-backdrop').forEach(backdrop => backdrop.remove());
        document.body.classList.remove('modal-open');
    }

    ['#editUserModal', '#deleteUserModal'].forEach(modalSelector => {
        const modalElement = document.querySelector(modalSelector);
        modalElement.addEventListener('hidden.bs.modal', clearModal);
    });

    async function fetchUsers() {
        try {
            const response = await fetch('/api/users');
            const users = await response.json();

            const usersTableBody = document.querySelector('#usersTable tbody');
            usersTableBody.innerHTML = '';
            const docFragment = document.createDocumentFragment();

            users.forEach(user => {
                let userRow = document.createElement('tr');
                let rolesList = user.roles.map(role => role.name.replace('ROLE_', '')).join(', ');

                userRow.innerHTML = `
                <td>${user.id}</td>
                <td>${user.username}</td>
                <td>${user.email}</td>
                <td>${rolesList}</td>
                <td>
                    <button data-user-id="${user.id}" class="edit-button btn btn-primary" data-bs-toggle="modal" data-bs-target="#editUserModal">Редактировать</button>
                    <button data-user-id="${user.id}" class="delete-button btn btn-danger" data-bs-toggle="modal" data-bs-target="#deleteUserModal">Удалить</button>
                </td>
            `;
                docFragment.appendChild(userRow);
            });

            usersTableBody.appendChild(docFragment);
        } catch (error) {
            console.error('Ошибка при получении пользователей:', error);
        }
    }

    document.querySelector('#usersTable tbody').addEventListener('click', function (event) {
        const target = event.target;
        const userId = target.getAttribute('data-user-id');
        console.log(userId);

        if (target.classList.contains('edit-button')) {
            showEditUserModal(userId);
        } else if (target.classList.contains('delete-button')) {
            showDeleteUserModal(userId);
        }
    });

    async function loadRoles() {
        try {
            const response = await fetch(`/api/users/roles`);
            if (response.ok) {
                const roles = await response.json();
                const rolesSelect = document.getElementById('editRoles');
                rolesSelect.innerHTML = '';

                roles.forEach(role => {
                    const option = document.createElement('option');
                    option.value = role.id;
                    option.textContent = role.name.replace('ROLE_', '');
                    rolesSelect.appendChild(option);
                });
            }
        } catch (error) {
            console.error('Ошибка при загрузке ролей:', error);
        }
    }

    // Открытие модального окна и обработка данных
    async function showEditUserModal(userId) {
        try {
            await loadRoles();

            const response = await fetch(`/api/users/${userId}`);
            if (response.ok) {
                const user = await response.json();
                document.getElementById('editUserId').value = user.id;
                document.getElementById('editUsername').value = user.username;
                document.getElementById('editEmail').value = user.email;
                document.getElementById('editPassword').value = user.password;


                // Выбор ролей пользователя
                const userRoles = new Set(user.roles.map(role => role.id));
                const rolesSelect = document.getElementById('editRoles');
                Array.from(rolesSelect.options).forEach(option => {
                    option.selected = userRoles.has(Number(option.value));
                });
            }

            const editModal = new bootstrap.Modal(document.getElementById('editUserModal'));
            editModal.show();
        } catch (error) {
            console.error('Ошибка:', error);
        }
    }

    document.getElementById('editUserForm').addEventListener('submit', async function (event) {
        event.preventDefault();

        const userId = document.getElementById('editUserId').value;
        const passwordValue = document.getElementById('editPassword').value;

        const userUpdateData = {
            username: document.getElementById('editUsername').value,
            email: document.getElementById('editEmail').value,
            password: passwordValue.trim() ? passwordValue : null,

            roles: Array.from(document.getElementById('editRoles').selectedOptions).map(option => {
                return {id: option.value}
            })
        };

        try {
            const response = await fetch(`/api/users/update/${userId}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(userUpdateData)
            });

            if (response.ok) {
                await fetchUsers();
            } else {
                throw new Error('Что-то пошло не так при обновлении пользователя');
            }
        } catch (error) {
            console.error('Ошибка:', error);
        }
        const editModal = bootstrap.Modal.getInstance(document.getElementById('editUserModal'));
        editModal.hide();
    });

    // Загрузка информации о пользователе для подтверждения его удаления
    async function showDeleteUserModal(userId) {
        try {
            const response = await fetch('/api/users/' + userId);
            if (response.ok) {
                const user = await response.json();

                document.getElementById('deleteUsername').value = user.username;
                document.getElementById('deleteEmail').value = user.email;
                document.getElementById('deleteRoles').value = user.roles.map(role => role.name).join(', ');

                document.getElementById('deleteUserId').value = user.id;

                document.getElementById('deleteUsername').readOnly = true;
                document.getElementById('deleteEmail').readOnly = true;
                document.getElementById('deleteRoles').readOnly = true;

                const deleteModal = new bootstrap.Modal(document.getElementById('deleteUserModal'));
                deleteModal.show();
            } else {
                throw new Error('Не удалось загрузить информацию о пользователе');
            }
        } catch (error) {
            console.error('Ошибка при показе модального окна удаления:', error);
        }
    }

// Удаление пользователя
    document.getElementById('deleteUserForm').addEventListener('submit', async function (event) {
        event.preventDefault();

        const userId = document.getElementById('deleteUserId').value;

        try {
            const response = await fetch(`/api/users/delete?id=${userId}`, {
                method: 'DELETE',
            });

            if (response.ok) {
                await fetchUsers();
                const deleteModal = bootstrap.Modal.getInstance(document.getElementById('deleteUserModal'));
                deleteModal.hide();
            } else {
                console.error('Не удалось удалить пользователя:', response.status);
            }
        } catch (error) {
            console.error('Ошибка при удалении пользователя:', error);
        }
    });

    document.querySelector('#createUserForm').addEventListener('submit', async function (e) {
        e.preventDefault();

        const username = document.getElementById('createUsername').value;
        const password = document.getElementById('createPassword').value;
        const email = document.getElementById('createEmail').value;

        const rolesIds = Array.from(document.getElementById('createRoles').selectedOptions)
            .map(option => option.value);

        const userData = {
            username: username,
            password: password,
            email: email,
        };

        const queryString = rolesIds.map(roleId => `rolesIds=${encodeURIComponent(roleId)}`).join('&');

        try {
            const response = await fetch(`/api/users/create?${queryString}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(userData)
            });

            if (response.ok) {
                console.log('Пользователь создан:', await response.json());
                await fetchUsers();
                document.querySelector('#users-tab').click();
                this.reset();
            } else {
                console.error('Ошибка при создании пользователя:', response.statusText);
            }
        } catch (error) {
            console.error('Ошибка при отправке данных на сервер:', error);
        }
    });

    function handleRoleChange() {
        fetchUsers();
        var selectedRole = document.querySelector('input[name="roleSelector"]:checked').value;

        if (selectedRole === "admin") {
            adminContent.style.display = 'block';
            userInfo.style.display = 'none';
        } else if (selectedRole === "user") {
            userInfo.style.display = 'block';
            adminContent.style.display = 'none';
        }
    }

    document.querySelectorAll('input[name="roleSelector"]').forEach(function (el) {
        el.addEventListener('change', handleRoleChange);
    });

    function initialRoleSetup() {
        fetch('/api/users/current')
            .then(response => {
                if (!response.ok) {
                    throw new Error('Сетевой ответ был не ok.');
                }
                return response.json();
            })
            .then(userData => {
                const rolesList = userData.roles.map(role => role.name.replace('ROLE_', ''));
                const isAdmin = rolesList.includes('ADMIN');

                if (isAdmin) {
                    userInfo.style.display = 'none';
                    adminContent.style.display = 'block';
                    document.getElementById('roleAdmin').checked = true;
                } else {
                    userInfo.style.display = 'block';
                    adminContent.style.display = 'none';
                    document.getElementById('roleUser').checked = true;
                }

                fetchUsers();
            })
            .catch(error => {
                console.error('Проблема с запросом текущего пользователя', error);
            });
    }

    var roleString = "[[${currentUser.roles}]]";
    initialRoleSetup(roleString);
    fetchAndUpdateCurrentUser();


});
