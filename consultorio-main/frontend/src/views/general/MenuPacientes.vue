<template>
  <div>
    <!-- Tarjeta de búsqueda -->
    <div class="card-paciente">
      <h2>Buscar Paciente</h2>
      <div class="form-grid">
        <div class="form-group">
          <label for="buscar_nombre">Nombre</label>
          <input
              type="text"
              id="buscar_nombre"
              v-model="busqueda.nombre"
              placeholder="Ej. Juan Pérez López"
          >
        </div>
      </div>
      <div class="form-actions">
        <button class="btn btn-primary" @click="buscarPacientes">
          <i class="fas fa-search"></i> Buscar
        </button>
        <button class="btn btn-warning" @click="limpiarBusqueda">
          <i class="fas fa-eraser"></i> Limpiar
        </button>
      </div>
    </div>

    <!-- Tarjeta listado de pacientes -->
    <div class="card-paciente">
      <h2>Listado de Pacientes</h2>
      <div class="table-responsive">
        <table class="tabla-pacientes" id="paciente_table">
          <thead>
          <tr>
            <th>ID</th>
            <th>Nombre</th>
            <th>Apellido Paterno</th>
            <th>Apellido Materno</th>
            <th>Fecha Nacimiento</th>
            <th>Sexo</th>
            <th style="text-align: center;">Acciones</th>
          </tr>
          </thead>
          <tbody id="tabla-body">
          <tr v-for="paciente in pacientesFiltrados" :key="paciente.id">
            <td>{{ paciente.id }}</td>
            <td>{{ paciente.nombre }}</td>
            <td>{{ paciente.apellidoPaterno }}</td>
            <td>{{ paciente.apellidoMaterno }}</td>
            <td>{{ paciente.fechaNacimiento }}</td>
            <td>{{ paciente.sexo }}</td>
            <td style="text-align: center;">
              <button class="btn-admin-paciente" @click="administrarPaciente(paciente)">
                <i class="fas fa-user-cog"></i> Administrar
              </button>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>

    <div class="form-actions">
      <button class="btn btn-primary" @click="nuevoPaciente">
        <i class="fas fa-user-plus"></i> Nuevo paciente
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();

// Estado
const busqueda = ref({
  nombre: ''
});

const pacientes = ref([
  {
    id: '001',
    nombre: 'Juan',
    apellidoPaterno: 'Pérez',
    apellidoMaterno: 'López',
    fechaNacimiento: '2005-03-12',
    sexo: 'M'
  },
  {
    id: '002',
    nombre: 'María',
    apellidoPaterno: 'Gómez',
    apellidoMaterno: 'Ramírez',
    fechaNacimiento: '2010-07-22',
    sexo: 'F'
  },
  {
    id: '003',
    nombre: 'Carlos',
    apellidoPaterno: 'Martínez',
    apellidoMaterno: 'Fernández',
    fechaNacimiento: '2008-11-05',
    sexo: 'M'
  }
]);

// Computed - Filtrado de pacientes
const pacientesFiltrados = computed(() => {
  if (!busqueda.value.nombre.trim()) {
    return pacientes.value;
  }

  const nombreBusqueda = busqueda.value.nombre.trim().toLowerCase();

  return pacientes.value.filter(paciente => {
    const nombreCompleto = `${paciente.nombre} ${paciente.apellidoPaterno} ${paciente.apellidoMaterno}`.toLowerCase();
    return nombreCompleto.includes(nombreBusqueda);
  });
});

// Funciones
const buscarPacientes = () => {
  // El filtrado es automático gracias al computed
  // Esta función puede hacer una petición al backend si es necesario
  console.log('Buscando:', busqueda.value.nombre);
};

const limpiarBusqueda = () => {
  busqueda.value.nombre = '';
};

const administrarPaciente = (paciente: any) => {
  // Navegar a la vista de administrar paciente con el ID
  router.push({
    name: 'AdministrarPaciente',
    params: { id: paciente.id },
    query: {
      nombre: paciente.nombre,
      apellidoPaterno: paciente.apellidoPaterno,
      apellidoMaterno: paciente.apellidoMaterno
    }
  });
};

const nuevoPaciente = () => {
  router.push({ name: 'AgregarPaciente' });
};
</script>

<style scoped>
/* Tarjeta blanca */
.card-paciente {
  background-color: #ffffff;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0px 4px 12px rgba(0, 0, 0, 0.1);
  margin-bottom: 30px;
}

.card-paciente h2 {
  margin-bottom: 20px;
  font-size: 22px;
  color: #333;
}

.form-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 15px;
}

.form-group label {
  font-weight: bold;
  color: #555;
  display: block;
  margin-bottom: 5px;
}

.form-group input {
  width: 100%;
  padding: 8px 10px;
  border-radius: 5px;
  border: 1px solid #ccc;
  font-size: 14px;
}

.form-group input:focus {
  outline: none;
  border-color: var(--primary-color);
  box-shadow: 0 0 0 2px rgba(56, 168, 214, 0.1);
}

.form-actions {
  margin-top: 20px;
  display: flex;
  gap: 15px;
  flex-wrap: wrap;
}

.table-responsive {
  overflow-x: auto;
}

/* Estilos de tabla de pacientes */
.tabla-pacientes {
  width: 100%;
  border-collapse: collapse;
  margin-top: 14px;
  background-color: var(--white-color);
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
}

.tabla-pacientes th,
.tabla-pacientes td {
  border: 1px solid #ddd;
  padding: 10px 14px;
  font-size: 15px;
  text-align: left;
  color: #333;
}

.tabla-pacientes th {
  background-color: var(--primary-color);
  color: rgb(255, 255, 255);
  font-weight: 600;
  text-align: center;
}

.tabla-pacientes td:first-child {
  width: 60px;
  text-align: center;
  font-weight: bold;
  color: #333;
}

.tabla-pacientes tr:nth-child(even) {
  background-color: #f0f6fb;
}

.tabla-pacientes tr:nth-child(odd) {
  background-color: #ffffff;
}

.tabla-pacientes tr:hover {
  background-color: #e2edf7;
}

/* Botón Administrar Paciente */
.btn-admin-paciente {
  padding: 8px 16px;
  background-color: var(--primary-color);
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-weight: 600;
  transition: all 0.3s;
  display: inline-flex;
  align-items: center;
  gap: 6px;
  text-decoration: none;
  font-size: 14px;
}

.btn-admin-paciente:hover {
  background-color: #2c8bb8;
  transform: translateY(-1px);
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
}

/* Botones generales */
.btn {
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-weight: 600;
  transition: all 0.3s;
  display: inline-flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
}

.btn-primary {
  background-color: var(--primary-color);
  color: white;
}

.btn-primary:hover {
  background-color: #2c8bb8;
  transform: translateY(-1px);
}

.btn-warning {
  background-color: #f39c12;
  color: white;
}

.btn-warning:hover {
  background-color: #e67e22;
}
</style>