// ===========================
// CONFIGURACIÓN Y CONSTANTES
// ===========================

const API_BASE_URL = 'http://localhost:8080/api';

function obtenerSlugDeURL() {

    const urlParams = new URLSearchParams(window.location.search);
    const negocioParam = urlParams.get('negocio');

    if (negocioParam) {
        return negocioParam;
    }

    const pathSegments = window.location.pathname
        .split('/')
        .filter(segment => segment && segment !== 'index.html');

    if (pathSegments.length > 0) {
        return pathSegments[pathSegments.length - 1];
    }

    // Si no encuentra nada, retornar null
    return null;
}

const NEGOCIO_SLUG = obtenerSlugDeURL();


if (!NEGOCIO_SLUG) {
    alert('Error: Debe especificar un negocio en la URL');
    throw new Error('No se especificó un slug de negocio');
}

// Horarios disponibles de lunes a viernes
const HORARIOS_DISPONIBLES = {
    inicio: 9,  // 9 AM
    fin: 18,    // 6 PM
    intervalo: 30 // minutos
};

// Estado global de la aplicación
const appState = {
    servicios: [],
        empleados: [],
        servicioSeleccionado: null,
        empleadoSeleccionado: null,
        fechaSeleccionada: null,
        horaSeleccionada: null,
        turnosExistentes: [],
        mesActual: new Date().getMonth(), // 0-11
        anioActual: new Date().getFullYear()
};

function cambiarMes(direccion) {
    // 1. Calcular el nuevo mes y año
    let nuevoMes = appState.mesActual + direccion;
    let nuevoAnio = appState.anioActual;

    if (nuevoMes > 11) {
        nuevoMes = 0;
        nuevoAnio++;
    } else if (nuevoMes < 0) {
        nuevoMes = 11;
        nuevoAnio--;
    }

    // 2. Comprobar que no se pueda ir a meses pasados (Restricción)
    const hoy = new Date();
    // Creamos una fecha que representa el primer día del mes al que queremos ir
    const fechaActualMostrada = new Date(nuevoAnio, nuevoMes, 1);
    // Creamos una fecha que representa el primer día del mes actual
    const fechaHoy = new Date(hoy.getFullYear(), hoy.getMonth(), 1);

    // Si el mes al que queremos ir es anterior al mes actual, no hacemos nada.
    if (fechaActualMostrada < fechaHoy) {
         return;
    }

    // 3. Actualizar el estado y renderizar
    appState.mesActual = nuevoMes;
    appState.anioActual = nuevoAnio;

    // Limpiar selección de fecha y horario al cambiar de mes
    appState.fechaSeleccionada = null;
    appState.horaSeleccionada = null;

    // Ocultar pasos de horario y cliente
    document.getElementById('step-horario').style.display = 'none';
    document.getElementById('step-cliente').style.display = 'none';

    renderizarCalendario();
}

// ===========================
// INICIALIZACIÓN
// ===========================

document.addEventListener('DOMContentLoaded', async () => {
    try {
        await cargarServicios();
        configurarEventListeners();
    } catch (error) {
        console.error('Error al inicializar:', error);
        mostrarError('Error al cargar los datos iniciales');
    }
});

// ===========================
// CARGA DE DATOS DESDE API
// ===========================

/**
 * Carga todos los servicios disponibles del negocio
 */
async function cargarServicios() {
    try {
       if (!NEGOCIO_SLUG) {
                    mostrarError('Error: Slug del negocio no encontrado en la URL.');
                    return;
       }

        // Usamos el endpoint público que creamos: /api/service/public/{slug}
        const url = `${API_BASE_URL}/service/public/${NEGOCIO_SLUG}`;
        console.log('Cargando servicios desde:', url); // Diagnóstico

        const response = await fetch(url);

        if (!response.ok) {
            // Si el backend devuelve un error 4xx o 5xx
            throw new Error(`Error ${response.status}: No se pudieron cargar los servicios.`);
        }

        const services = await response.json();
        appState.servicios = services; // Almacenar en el estado global

        // Renderizar los servicios en el HTML
        renderizarServicios();

    } catch (error) {
        console.error('Fallo la carga de servicios:', error);
        mostrarError('No se pudo establecer conexión con el servidor o cargar servicios.');
    }
}

/**
 * Carga los empleados activos para un servicio específico
 */
async function cargarEmpleadosPorServicio(servicioId) {
    try {
        const response = await fetch(`${API_BASE_URL}/appointments/empleados/${servicioId}`);

        if (!response.ok) {
            throw new Error('No se pudieron cargar los empleados');
        }

        appState.empleados = await response.json();
        renderizarEmpleados();
    } catch (error) {
        console.error('Error al cargar empleados:', error);
        mostrarError('No se pudieron cargar los empleados disponibles');
    }
}

/**
 * Carga los turnos existentes para una fecha específica
 */
async function cargarTurnosPorFecha(fecha, empleadoId) {
    try {
        // Aquí necesitarías un endpoint en tu backend para obtener turnos por fecha
        // Por ahora simulamos la estructura
        const response = await fetch(
            `${API_BASE_URL}/appointments/turnos?fecha=${fecha}&empleadoId=${empleadoId}`
        );

        if (response.ok) {
            appState.turnosExistentes = await response.json();
        } else {
            appState.turnosExistentes = [];
        }

        renderizarHorarios();
    } catch (error) {
        console.error('Error al cargar turnos:', error);
        appState.turnosExistentes = [];
        renderizarHorarios();
    }
}

// ===========================
// RENDERIZADO DE COMPONENTES
// ===========================

/**
 * Renderiza la lista de servicios disponibles
 */
function renderizarServicios() {
    // ❗ CORRECCIÓN: Usar 'servicios-list' para aplicar el CSS Grid
    const servicesContainer = document.getElementById('servicios-list');
    servicesContainer.innerHTML = ''; // Limpiar contenido anterior

    if (appState.servicios.length === 0) {
        servicesContainer.innerHTML = '<p>No hay servicios disponibles en este momento.</p>';
        return;
    }

    appState.servicios.forEach(service => {
        // *** MODIFICACIÓN: Usar la nueva estructura de tarjeta de servicio con ícono fijo ***
        const card = document.createElement('div');
        card.className = 'servicio-card'; // Clase para el nuevo estilo
        card.dataset.id = service.id;
        card.dataset.servicioId = service.id; // Añadir data-servicio-id para la función seleccionarServicio
        card.innerHTML = `
            <div class="servicio-icon">
                <i class="fas fa-cut"></i> </div>
            <div class="servicio-info">
                <div class="servicio-nombre">${service.nombre}</div>
                <div class="servicio-descripcion">${service.descripcion || 'Sin descripción.'}</div>
                <div class="servicio-detalles">
                    <span><i class="fas fa-clock"></i> ${service.duracion} min</span>
                    <span><i class="fas fa-dollar-sign"></i> ${service.precio}</span>
                </div>
            </div>
        `;

        // Evento para seleccionar el servicio
        card.addEventListener('click', () => seleccionarServicio(service.id));

        servicesContainer.appendChild(card);
    });
}

/**
 * Renderiza la lista de empleados disponibles
 */
function renderizarEmpleados() {
    const container = document.getElementById('empleados-list');

    if (!appState.empleados || appState.empleados.length === 0) {
        container.innerHTML = '<p class="text-center text-gray-500">No hay empleados disponibles</p>';
        return;
    }

    container.innerHTML = appState.empleados
        .filter(empleado => empleado.activo)
        .map(empleado => `
            <div class="empleado-card"
                 data-empleado-id="${empleado.id}"
                 onclick="seleccionarEmpleado(${empleado.id})">
                <div class="empleado-avatar">
                    <i class="fas fa-user"></i>
                </div>
                <div class="empleado-info">
                    <h4>${empleado.nombreUsuario}</h4>
                    <span class="empleado-rol">${empleado.rolNegocio}</span>
                </div>
            </div>
        `).join('');
}

/**
 * Renderiza el calendario de fechas disponibles
 */
function renderizarCalendario() {
    const container = document.getElementById('calendario-container');
    const hoy = new Date();
    hoy.setHours(0, 0, 0, 0); // Normalizar el día de hoy

    // Nombres de meses en español
    const nombresMeses = ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio',
                          'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'];

    // Determinar el último día del mes actual en el estado
    const ultimoDiaMes = new Date(appState.anioActual, appState.mesActual + 1, 0);
    const numDiasMes = ultimoDiaMes.getDate();

    // 1. Crear el HTML del encabezado de navegación
    let calendarioHTML = `
        <div class="calendario-header">
            <button class="calendario-nav-btn" onclick="cambiarMes(-1)">
                <i class="fas fa-chevron-left"></i>
            </button>
            <div class="calendario-mes">
                ${nombresMeses[appState.mesActual]} ${appState.anioActual}
            </div>
            <button class="calendario-nav-btn" onclick="cambiarMes(1)">
                <i class="fas fa-chevron-right"></i>
            </button>
        </div>
        <div class="calendario-dias">
    `;

    // 2. Iterar por todos los días del mes
    for (let dia = 1; dia <= numDiasMes; dia++) {
        const fecha = new Date(appState.anioActual, appState.mesActual, dia);
        fecha.setHours(0, 0, 0, 0);

        const diaSemana = fecha.getDay(); // 0 (Dom) a 6 (Sáb)

        // 3. Verificar si el día ya pasó o si es fin de semana
        const fechaStr = fecha.toISOString().split('T')[0];
        // Deshabilita días pasados, Sábados (6) y Domingos (0)
        const estaDeshabilitado = fecha < hoy || diaSemana === 0 || diaSemana === 6;

        const nombreDia = fecha.toLocaleDateString('es-ES', { weekday: 'short' });
        const numeroDia = fecha.getDate();

        // Clases CSS
        let clases = 'dia-card';
        if (fecha.getTime() === hoy.getTime()) {
            clases += ' hoy';
        }
        if (estaDeshabilitado) {
            clases += ' deshabilitado';
        }
        if (appState.fechaSeleccionada === fechaStr) {
            clases += ' selected';
        }

        calendarioHTML += `
            <div class="${clases}"
                 data-fecha="${fechaStr}"
                 onclick="${estaDeshabilitado ? '' : `seleccionarFecha('${fechaStr}')`}"
                 style="${estaDeshabilitado ? 'opacity: 0.5; cursor: not-allowed;' : ''}">
                <div class="dia-nombre">${nombreDia}</div>
                <div class="dia-numero">${numeroDia}</div>
            </div>
        `;
    }

    calendarioHTML += '</div>';
    container.innerHTML = calendarioHTML;
}

/**
 * Renderiza los horarios disponibles para la fecha seleccionada
 */
function renderizarHorarios() {
    const container = document.getElementById('horarios-list');

    if (!appState.fechaSeleccionada) {
        container.innerHTML = '<p class="text-center text-gray-500">Selecciona una fecha</p>';
        return;
    }

    const horarios = generarHorariosDisponibles();

    if (horarios.length === 0) {
        container.innerHTML = '<p class="text-center text-gray-500">No hay horarios disponibles para esta fecha</p>';
        return;
    }

    container.innerHTML = horarios.map(horario => {
        const estaOcupado = verificarHorarioOcupado(horario);

        return `
            <button class="horario-btn ${estaOcupado ? 'ocupado' : ''}"
                    data-hora="${horario.inicio}"
                    onclick="seleccionarHorario('${horario.inicio}', '${horario.fin}')"
                    ${estaOcupado ? 'disabled' : ''}>
                ${formatearHora(horario.inicio)}
            </button>
        `;
    }).join('');
}

// ===========================
// GENERACIÓN DE HORARIOS
// ===========================

/**
 * Genera todos los horarios disponibles según la configuración
 */
function generarHorariosDisponibles() {
    const horarios = [];
    const { inicio, fin, intervalo } = HORARIOS_DISPONIBLES;

    // Obtener duración del servicio seleccionado
    const servicio = appState.servicios.find(s => s.id === appState.servicioSeleccionado);
    const duracionServicio = servicio ? servicio.duracion : 30;

    let horaActual = inicio * 60; // Convertir a minutos
    const horaFin = fin * 60;

    while (horaActual + duracionServicio <= horaFin) {
        const horaInicio = minutosAHora(horaActual);
        const horaFinal = minutosAHora(horaActual + duracionServicio);

        horarios.push({
            inicio: horaInicio,
            fin: horaFinal
        });

        horaActual += intervalo;
    }

    return horarios;
}

/**
 * Verifica si un horario está ocupado
 */
function verificarHorarioOcupado(horario) {
    if (!appState.turnosExistentes || appState.turnosExistentes.length === 0) {
        return false;
    }

    const OFFSET_NEGOCIO = '-03:00';
    const fechaBase = appState.fechaSeleccionada;

    const horarioInicioString = `${fechaBase}T${horario.inicio}:00${OFFSET_NEGOCIO}`;
    const horarioFinString = `${fechaBase}T${horario.fin}:00${OFFSET_NEGOCIO}`;

    const horarioInicio = new Date(horarioInicioString);
    const horarioFin = new Date(horarioFinString);

    return appState.turnosExistentes.some(turno => {

        const turnoInicio = new Date(turno.horaInicio);
        const turnoFin = new Date(turno.horaFinal);

        return (horarioInicio < turnoFin && horarioFin > turnoInicio);
    });
}
// ===========================
// FUNCIONES DE SELECCIÓN
// ===========================

/**
 * Maneja la selección de un servicio
 */
async function seleccionarServicio(servicioId) {
    // Remover selección previa
    document.querySelectorAll('.servicio-card').forEach(card => {
        card.classList.remove('selected');
    });

    // Marcar como seleccionado
    // ❗ CORRECCIÓN: Ahora buscamos por data-servicio-id, que lo agregamos en renderizarServicios
    const card = document.querySelector(`[data-servicio-id="${servicioId}"]`);
    if (card) card.classList.add('selected');

    appState.servicioSeleccionado = servicioId;
    appState.empleadoSeleccionado = null;
    appState.fechaSeleccionada = null;
    appState.horaSeleccionada = null;

    // Mostrar siguiente paso
    document.getElementById('step-empleado').style.display = 'block';
    document.getElementById('step-empleado').scrollIntoView({ behavior: 'smooth' });

    // Cargar empleados
    await cargarEmpleadosPorServicio(servicioId);
}

/**
 * Maneja la selección de un empleado
 */
function seleccionarEmpleado(empleadoId) {
    // Remover selección previa
    document.querySelectorAll('.empleado-card').forEach(card => {
        card.classList.remove('selected');
    });

    // Marcar como seleccionado
    const card = document.querySelector(`[data-empleado-id="${empleadoId}"]`);
    if (card) card.classList.add('selected');

    appState.empleadoSeleccionado = empleadoId;
    appState.fechaSeleccionada = null;
    appState.horaSeleccionada = null;

    // Mostrar siguiente paso
    document.getElementById('step-fecha').style.display = 'block';
    document.getElementById('step-fecha').scrollIntoView({ behavior: 'smooth' });

    renderizarCalendario();
}

/**
 * Maneja la selección de una fecha
 */
async function seleccionarFecha(fecha) {
    // Remover selección previa
    document.querySelectorAll('.dia-card').forEach(card => {
        card.classList.remove('selected');
    });

    // Marcar como seleccionado
    const card = document.querySelector(`[data-fecha="${fecha}"]`);
    if (card) card.classList.add('selected');

    appState.fechaSeleccionada = fecha;
    appState.horaSeleccionada = null;

    // Mostrar siguiente paso
    document.getElementById('step-horario').style.display = 'block';
    document.getElementById('step-horario').scrollIntoView({ behavior: 'smooth' });

    // Cargar turnos existentes y renderizar horarios
    await cargarTurnosPorFecha(fecha, appState.empleadoSeleccionado);
}

/**
 * Maneja la selección de un horario
 */
function seleccionarHorario(horaInicio, horaFin) {
    // Remover selección previa
    document.querySelectorAll('.horario-btn').forEach(btn => {
        btn.classList.remove('selected');
    });

    // Marcar como seleccionado
    const btn = document.querySelector(`[data-hora="${horaInicio}"]`);
    if (btn) btn.classList.add('selected');

    appState.horaSeleccionada = { inicio: horaInicio, fin: horaFin };

    // Mostrar formulario de cliente
    document.getElementById('step-cliente').style.display = 'block';
    document.getElementById('step-cliente').scrollIntoView({ behavior: 'smooth' });
}

// ===========================
// ENVÍO DE FORMULARIO
// ===========================

/**
 * Configura los event listeners
 */
function configurarEventListeners() {
    const form = document.getElementById('cliente-form');
    if (form) {
        form.addEventListener('submit', handleSubmitReserva);
    }
}

/**
 * Maneja el envío del formulario de reserva
 */
async function handleSubmitReserva(e) {
    e.preventDefault();

    // Validar que se hayan seleccionado todos los datos necesarios
    if (!appState.servicioSeleccionado || !appState.empleadoSeleccionado ||
        !appState.fechaSeleccionada || !appState.horaSeleccionada) {
        mostrarError('Por favor completa todos los pasos antes de confirmar');
        return;
    }

    // Obtener datos del formulario
    const formData = new FormData(e.target);

    const ZONA_HORARIA_OFFSET = ':00-03:00';

    // Crear objeto de datos para enviar
    const turnoData = {
        fechaTurno: appState.fechaSeleccionada,
        horaInicio: `${appState.fechaSeleccionada}T${appState.horaSeleccionada.inicio}${ZONA_HORARIA_OFFSET}`,
        horaFinal: `${appState.fechaSeleccionada}T${appState.horaSeleccionada.fin}${ZONA_HORARIA_OFFSET}`,
        estado: 'PENDIENTE',
        notas: formData.get('notas') || '',
        servicioId: appState.servicioSeleccionado,
        usuarioNegocioId: appState.empleadoSeleccionado,
        cliente: {
            nombre: formData.get('nombre') || '',
            email: formData.get('email') || '',
            telefono: formData.get('telefono') || '',
            notas: formData.get('notas') || '',
            negocioId: appState.servicios.find(s => s.id === appState.servicioSeleccionado)?.negocioId || null

        }
    };

    try {
        // Mostrar loader
        mostrarLoader(true);

        const response = await fetch(`${API_BASE_URL}/appointments/${NEGOCIO_SLUG}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(turnoData)
        });

        if (!response.ok) {
                let errorMessage = 'Error al crear la reserva.';

                try {
                    const errorData = await response.json();

                    // CORRECCIÓN CLAVE: Buscar de forma segura 'message', 'error', o cualquier campo que tu backend use.
                    // Los errores de Spring Boot/Jackson a menudo usan 'message' o 'error'.
                    errorMessage = errorData.message || errorData.error || errorData.nombre || errorMessage;

                } catch (e) {
                    // Si no es JSON (ej. HTML o texto simple), usamos el status text
                    errorMessage = response.statusText;
                }

                // Lanzamos el error con el mensaje limpio
                throw new Error(errorMessage);
            }

        const resultado = await response.json();

        // Mostrar mensaje de éxito
        mostrarExito('¡Reserva creada exitosamente!');

        // Reiniciar formulario y estado
        resetearFormulario();

    } catch (error) {
        console.error('Error al crear reserva:', error);
        mostrarError(error.message || 'No se pudo crear la reserva. Por favor intenta nuevamente.');
    } finally {
        mostrarLoader(false);
    }
}

// ===========================
// FUNCIONES AUXILIARES
// ===========================

/**
 * Convierte minutos a formato HH:MM
 */
function minutosAHora(minutos) {
    const horas = Math.floor(minutos / 60);
    const mins = minutos % 60;
    return `${horas.toString().padStart(2, '0')}:${mins.toString().padStart(2, '0')}`;
}

/**
 * Formatea una hora para mostrar (sin segundos)
 */
function formatearHora(hora) {
    return hora.substring(0, 5);
}

/**
 * Reinicia el formulario y el estado de la aplicación
 */
function resetearFormulario() {
    document.getElementById('cliente-form').reset();

    // Reiniciar estado
    appState.servicioSeleccionado = null;
    appState.empleadoSeleccionado = null;
    appState.fechaSeleccionada = null;
    appState.horaSeleccionada = null;

    // Ocultar pasos
    document.querySelectorAll('[id^="step-"]').forEach(step => {
        if (step.id !== 'step-servicio') {
            step.style.display = 'none';
        }
    });

    // Remover selecciones visuales
    document.querySelectorAll('.selected').forEach(el => {
        el.classList.remove('selected');
    });

    // Scroll al inicio
    window.scrollTo({ top: 0, behavior: 'smooth' });
}

/**
 * Muestra un mensaje de error
 */
function mostrarError(mensaje) {
    // Podrías usar una librería de notificaciones o crear tu propio sistema
    alert('Error: ' + mensaje);
}

/**
 * Muestra un mensaje de éxito
 */
function mostrarExito(mensaje) {
    alert('Éxito: ' + mensaje);
}

/**
 * Muestra u oculta un loader
 */
function mostrarLoader(mostrar) {
    const loader = document.getElementById('loader');
    if (loader) {
        loader.style.display = mostrar ? 'flex' : 'none';
    }
}