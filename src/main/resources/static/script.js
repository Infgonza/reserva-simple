const DOMAIN_SLUG = window.location.pathname.split('/')[1] || 'default-negocio'; // Intenta obtener el slug de la URL

async function loadInitialData() {
    // 1. Fetch los datos del backend
    try {
        const response = await fetch(`/api/web/public/data/${DOMAIN_SLUG}`); // Usa el slug
        if (!response.ok) throw new Error('Error al cargar los datos del negocio');
        
        const data = await response.json(); // Data es tu PublicDataResponse

        // 2. inyectar datos de perfil (nombre e imagen)
        document.querySelector('.profile-image').src = data.profileImageUrl;
        document.querySelector('.business-name').textContent = data.businessName;

        // 3. rellenar el dropdown de servicios
        renderServices(data.services);

    } catch (error) {
        console.error("Error en la carga inicial:", error);
        document.querySelector('.business-name').textContent = "Negocio no encontrado";
    }
}

function renderServices(services) {
    serviceSelect.innerHTML = '<option value="">Selecciona un servicio</option>';
    services.forEach(service => {
        const option = document.createElement('option');
        option.value = service.id; // Usar el ID del backend
        option.textContent = service.name;
        serviceSelect.appendChild(option);
    });
}


document.addEventListener('DOMContentLoaded', () => {
    loadInitialData();
    const serviceSelect = document.getElementById('service');
    const professionalSelect = document.getElementById('professional');
    const currentMonthYearSpan = document.getElementById('currentMonthYear');
    const calendarGrid = document.getElementById('calendarGrid');
    const timeGrid = document.getElementById('timeGrid');
    const prevMonthBtn = document.getElementById('prevMonth');
    const nextMonthBtn = document.getElementById('nextMonth');
    const doneDateSelectionBtn = document.getElementById('doneDateSelection');
    const confirmButton = document.getElementById('confirmButton');

    const sections = {
        service: document.getElementById('section-service'),
        professional: document.getElementById('section-professional'),
        datetime: document.getElementById('section-datetime'),
        payment: document.getElementById('section-payment'),
        personalInfo: document.getElementById('section-personal-info')
    };

    let selectedService = '';
    let selectedProfessional = '';
    let selectedDate = null;
    let selectedTime = null;
    let currentMonth = new Date(); // Inicia con el mes actual (o el que quieras)

    // --- DATOS DE EJEMPLO (En una app real, vendrían de tu backend) ---
    const mockData = {
        services: {
            peluqueria: {
                name: "Peluquería",
                professionals: [
                    { id: 'pedro', name: 'Pedro Rodríguez' },
                    { id: 'maria', name: 'María López' }
                ]
            },
            manicura: {
                name: "Manicura",
                professionals: [
                    { id: 'ana', name: 'Ana García' }
                ]
            },
            masajes: {
                name: "Masajes",
                professionals: [
                    { id: 'juan', name: 'Juan Pérez' }
                ]
            }
        },
        // Horarios disponibles por profesional y día (ejemplo simplificado)
        availableTimes: {
            'pedro': {
                '2025-11-04': ['09:00', '09:30', '10:00', '11:00', '12:00', '12:30'],
                '2025-11-05': ['10:00', '10:30', '11:00', '11:30', '13:00', '13:30'],
                '2025-11-12': ['14:00', '14:30', '15:00', '15:30', '16:00', '16:30']
            },
            'ana': {
                '2025-11-07': ['09:00', '09:30', '10:00', '10:30'],
                '2025-11-22': ['11:00', '11:30', '12:00', '12:30']
            },
            // ... más datos
        }
    };

    // --- FUNCIONES DE VISIBILIDAD DE SECCIONES ---
    function showSection(sectionElement) {
        sectionElement.classList.remove('hidden');
    }

    function hideSection(sectionElement) {
        sectionElement.classList.add('hidden');
    }

    function showAllRemainingSections() {
        showSection(sections.payment);
        showSection(sections.personalInfo);
        confirmButton.classList.remove('hidden');
    }

    // --- LÓGICA DE SELECCIÓN DE SERVICIO ---
    serviceSelect.addEventListener('change', () => {
        selectedService = serviceSelect.value;
        professionalSelect.innerHTML = '<option value="">Selecciona un profesional</option>'; // Limpiar

        if (selectedService) {
            const professionals = mockData.services[selectedService].professionals;
            //const relevantProfessionals = filterProfessionalsByService(selectedService, data.allProfessionals);
            professionals.forEach(prof => {
                const option = document.createElement('option');
                option.value = prof.id;
                option.textContent = prof.name;
                professionalSelect.appendChild(option);
            });
            showSection(sections.professional);
            // Reiniciar selecciones posteriores
            selectedProfessional = '';
            selectedDate = null;
            selectedTime = null;
            hideSection(sections.datetime);
            hideSection(sections.payment);
            hideSection(sections.personalInfo);
            confirmButton.classList.add('hidden');
        } else {
            hideSection(sections.professional);
            hideSection(sections.datetime);
            hideSection(sections.payment);
            hideSection(sections.personalInfo);
            confirmButton.classList.add('hidden');
        }
    });

    // --- LÓGICA DE SELECCIÓN DE PROFESIONAL ---
    professionalSelect.addEventListener('change', () => {
        selectedProfessional = professionalSelect.value;
        if (selectedProfessional) {
            showSection(sections.datetime);
            renderCalendar(); // Renderiza el calendario al seleccionar profesional
            // Reiniciar selecciones posteriores
            selectedDate = null;
            selectedTime = null;
            hideSection(sections.payment);
            hideSection(sections.personalInfo);
            confirmButton.classList.add('hidden');
        } else {
            hideSection(sections.datetime);
            hideSection(sections.payment);
            hideSection(sections.personalInfo);
            confirmButton.classList.add('hidden');
        }
    });

    // --- LÓGICA DEL CALENDARIO ---
    function renderCalendar() {
        calendarGrid.innerHTML = ''; // Limpiar calendario
        timeGrid.innerHTML = ''; // Limpiar horarios
        selectedDate = null; // Resetear fecha seleccionada
        
        const today = new Date();
        const firstDayOfMonth = new Date(currentMonth.getFullYear(), currentMonth.getMonth(), 1);
        const lastDayOfMonth = new Date(currentMonth.getFullYear(), currentMonth.getMonth() + 1, 0);
        const daysInMonth = lastDayOfMonth.getDate();
        const startDayOfWeek = firstDayOfMonth.getDay(); // 0 = Domingo, 1 = Lunes

        currentMonthYearSpan.textContent = currentMonth.toLocaleDateString('es', { month: 'long', year: 'numeric' }).replace(/\b\w/g, c => c.toUpperCase());

        // Días de la semana
        const dayLabels = ['Dom', 'Lun', 'Mar', 'Mié', 'Jue', 'Vie', 'Sáb'];
        dayLabels.forEach(label => {
            const span = document.createElement('span');
            span.textContent = label;
            span.classList.add('day-label');
            calendarGrid.appendChild(span);
        });

        // Días del mes anterior (grises)
        const prevMonthLastDay = new Date(currentMonth.getFullYear(), currentMonth.getMonth(), 0).getDate();
        for (let i = startDayOfWeek === 0 ? 6 : startDayOfWeek - 1; i > 0; i--) { // Ajuste para que Lunes sea el primer día
            const span = document.createElement('span');
            span.textContent = prevMonthLastDay - i + 1;
            span.classList.add('day-disabled');
            calendarGrid.appendChild(span);
        }

        // Días del mes actual
        for (let i = 1; i <= daysInMonth; i++) {
            const day = new Date(currentMonth.getFullYear(), currentMonth.getMonth(), i);
            const span = document.createElement('span');
            span.textContent = i;
            span.classList.add('day-available');

            if (day.toDateString() === today.toDateString()) {
                span.classList.add('day-today');
            }
            if (day < today) { // Deshabilitar días pasados
                span.classList.remove('day-available');
                span.classList.add('day-disabled');
            } else {
                span.addEventListener('click', () => {
                    if (!span.classList.contains('day-disabled')) {
                        // Desmarcar día anterior
                        document.querySelectorAll('.calendar-grid .day-selected').forEach(s => s.classList.remove('day-selected'));
                        span.classList.add('day-selected');
                        selectedDate = day;
                        renderTimeSlots(); // Renderizar horarios para el día seleccionado
                    }
                });
            }
            calendarGrid.appendChild(span);
        }
        
        // Días del siguiente mes (grises)
        const totalCells = calendarGrid.children.length;
        const remainingCells = 42 - totalCells; // 6 filas * 7 días
        for (let i = 1; i <= remainingCells; i++) {
            const span = document.createElement('span');
            span.textContent = i;
            span.classList.add('day-disabled');
            calendarGrid.appendChild(span);
        }
    }

    function renderTimeSlots() {
        timeGrid.innerHTML = '';
        selectedTime = null; // Resetear hora seleccionada
        hideSection(sections.payment); // Ocultar secciones posteriores
        hideSection(sections.personalInfo);
        confirmButton.classList.add('hidden');

        if (!selectedProfessional || !selectedDate) {
            return;
        }

        const dateKey = selectedDate.toISOString().split('T')[0];
        const availableSlots = mockData.availableTimes[selectedProfessional]?.[dateKey] || [];

        if (availableSlots.length === 0) {
            const noSlotsMessage = document.createElement('div');
            noSlotsMessage.textContent = 'No hay horarios disponibles para este día.';
            noSlotsMessage.style.textAlign = 'center';
            noSlotsMessage.style.marginTop = '10px';
            timeGrid.appendChild(noSlotsMessage);
            return;
        }

        availableSlots.forEach(slot => {
            const span = document.createElement('span');
            span.textContent = slot;
            span.classList.add('time-slot');
            span.addEventListener('click', () => {
                // 1. Desmarcar hora anterior
                document.querySelectorAll('.time-grid .selected').forEach(s => s.classList.remove('selected'));
                span.classList.add('selected');
                
                // 2. Actualizar estado
                selectedTime = slot;
                
                // 3. DISPARADOR DE FLUJO: Mostrar el resto del formulario
                showAllRemainingSections(); 
            });
            timeGrid.appendChild(span);
        });
    }

    prevMonthBtn.addEventListener('click', () => {
        currentMonth.setMonth(currentMonth.getMonth() - 1);
        renderCalendar();
    });

    nextMonthBtn.addEventListener('click', () => {
        currentMonth.setMonth(currentMonth.getMonth() + 1);
        renderCalendar();
    });
    


    // --- LÓGICA DEL FORMULARIO DE ENVÍO ---
    document.getElementById('bookingForm').addEventListener('submit', (e) => {
        e.preventDefault();
        if (!selectedService || !selectedProfessional || !selectedDate || !selectedTime) {
            alert('Por favor, completa todos los pasos de la reserva.');
            return;
        }

        const formData = new FormData(e.target);
        const bookingDetails = {
            serviceId: selectedService,
            professionalId: selectedProfessional,
            date: selectedDate.toISOString().split('T')[0],
            time: selectedTime,
            paymentMethod: formData.get('paymentMethod'),
            fullName: formData.get('fullName'),
            email: formData.get('email'),
            phone: formData.get('phone')
        };

        console.log('Datos de la Reserva:', bookingDetails);
        alert('Reserva Confirmada! Revisa la consola para ver los detalles.');
        // Aquí enviarías 'bookingDetails' a tu backend (ej. usando fetch API)
        // fetch('/api/book-appointment', {
        //     method: 'POST',
        //     headers: { 'Content-Type': 'application/json' },
        //     body: JSON.stringify(bookingDetails)
        // })
        // .then(response => response.json())
        // .then(data => console.log('Reserva exitosa:', data))
        // .catch(error => console.error('Error en la reserva:', error));
    });

    // --- INICIALIZACIÓN ---
    // Asegúrate de que solo la primera sección sea visible al cargar
    Object.keys(sections).forEach(key => {
        if (key !== 'service') {
            sections[key].classList.add('hidden');
        }
    });
    confirmButton.classList.add('hidden');

    // Renderizar el calendario inicial (puede ser el mes actual por defecto)
    renderCalendar();
});