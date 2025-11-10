<template>
  <div>
    <div class="payment-container">
      <div class="payment-header">
        <h2 class="payment-title">Registro de Pagos</h2>
        <button class="btn btn-primary" @click="abrirModal()">
          <i class="fas fa-plus"></i> Registrar Pago
        </button>
      </div>

      <!-- Tarjetas de resumen -->
      <div class="payment-cards">
        <div class="payment-card">
          <h3 class="payment-card-title">
            <i class="fas fa-money-bill-wave"></i> Total Pagado Hoy
          </h3>
          <div class="payment-card-value">${{ totalPagadoHoy.toFixed(2) }}</div>
        </div>
        <div class="payment-card">
          <h3 class="payment-card-title">
            <i class="fas fa-calendar-check"></i> Citas con Anticipo
          </h3>
          <div class="payment-card-value">{{ citasConAnticipo }}</div>
        </div>
        <div class="payment-card">
          <h3 class="payment-card-title">
            <i class="fas fa-exclamation-circle"></i> Pendientes de Pago
          </h3>
          <div class="payment-card-value">{{ pendientesPago }}</div>
        </div>
      </div>

      <!-- Tabla de pagos -->
      <div class="payment-table-container">
        <h3 style="margin-bottom: 15px; color: var(--primary-color);">
          Historial de Pagos
        </h3>
        <table class="payment-table">
          <thead>
          <tr>
            <th>Paciente</th>
            <th>Fecha Cita</th>
            <th>Monto Total</th>
            <th>Anticipo (50%)</th>
            <th>Saldo Pendiente</th>
            <th>Estado</th>
            <th>Acciones</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="payment in payments" :key="payment.id">
            <td>{{ payment.patient }}</td>
            <td>{{ formatDate(payment.appointmentDate) }}</td>
            <td>${{ payment.totalAmount.toFixed(2) }}</td>
            <td>${{ calcularAnticipo(payment).toFixed(2) }}</td>
            <td>${{ calcularSaldo(payment).toFixed(2) }}</td>
            <td>
                <span :class="['status-badge', getStatusClass(payment)]">
                  {{ getStatusText(payment) }}
                </span>
            </td>
            <td>
              <button
                  v-if="payment.paymentType === 'advance'"
                  class="action-btn btn-success"
                  @click="completarPago(payment.id)"
                  title="Completar pago"
              >
                <i class="fas fa-check-circle"></i>
              </button>
              <button
                  class="action-btn btn-primary"
                  @click="abrirModal(payment.id)"
                  title="Editar"
              >
                <i class="fas fa-edit"></i>
              </button>
              <button
                  class="action-btn btn-danger"
                  @click="confirmarEliminar(payment.id)"
                  title="Eliminar"
              >
                <i class="fas fa-trash"></i>
              </button>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- Modal de pago -->
    <div v-if="showModal" class="modal" @click.self="cerrarModal">
      <div class="modal-content">
        <div class="modal-header">
          <h3 class="modal-title">{{ modalTitle }}</h3>
          <button class="close-btn" @click="cerrarModal">&times;</button>
        </div>
        <form @submit.prevent="guardarPago">
          <div class="form-group">
            <label class="form-label" for="patient">Paciente</label>
            <select class="form-select" v-model="formData.patient" required>
              <option value="">Seleccionar Paciente</option>
              <option value="Juan Pérez">Juan Pérez</option>
              <option value="María García">María García</option>
              <option value="Ana Rodríguez">Ana Rodríguez</option>
              <option value="Luis Martínez">Luis Martínez</option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-label" for="appointment-date">Fecha de Cita</label>
            <input
                type="date"
                class="form-input"
                v-model="formData.appointmentDate"
                required
            >
          </div>
          <div class="form-group">
            <label class="form-label" for="total-amount">Monto Total</label>
            <input
                type="number"
                class="form-input"
                v-model.number="formData.totalAmount"
                placeholder="0.00"
                step="0.01"
                min="0"
                required
            >
          </div>
          <div class="form-group">
            <label class="form-label" for="payment-type">Tipo de Pago</label>
            <select class="form-select" v-model="formData.paymentType" required>
              <option value="">Seleccionar tipo</option>
              <option value="advance">Anticipo (50%)</option>
              <option value="complete">Pago Completo</option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-label" for="payment-date">Fecha de Pago</label>
            <input
                type="date"
                class="form-input"
                v-model="formData.paymentDate"
                required
            >
          </div>
          <div v-if="isEditing" class="form-group">
            <label class="form-label" for="password">Contraseña</label>
            <input
                type="password"
                class="form-input"
                v-model="formData.password"
                placeholder="Requerida para editar"
            >
          </div>
          <div class="form-actions">
            <button type="submit" class="btn btn-primary">
              <i class="fas fa-save"></i> Guardar
            </button>
            <button type="button" class="btn btn-danger" @click="cerrarModal">
              <i class="fas fa-times"></i> Cancelar
            </button>
          </div>
        </form>
      </div>
    </div>

    <!-- Modal de confirmación -->
    <div v-if="showConfirmModal" class="modal" @click.self="cerrarConfirmModal">
      <div class="modal-content">
        <div class="modal-header">
          <h3 class="modal-title">Confirmar Eliminación</h3>
          <button class="close-btn" @click="cerrarConfirmModal">&times;</button>
        </div>
        <div style="padding: 20px 0;">
          <p>¿Está seguro de que desea eliminar este registro de pago?</p>
        </div>
        <div class="form-actions">
          <button type="button" class="btn" @click="cerrarConfirmModal">
            Cancelar
          </button>
          <button type="button" class="btn btn-danger" @click="eliminarPago">
            Eliminar
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue';

// Estado
const payments = ref([
  {
    id: 1,
    patient: "Juan Pérez",
    appointmentDate: "2025-09-15",
    totalAmount: 200,
    paymentType: "advance",
    paymentDate: "2025-09-10"
  },
  {
    id: 2,
    patient: "María García",
    appointmentDate: "2025-09-16",
    totalAmount: 300,
    paymentType: "complete",
    paymentDate: "2025-09-11"
  },
]);

const showModal = ref(false);
const showConfirmModal = ref(false);
const isEditing = ref(false);
const modalTitle = ref('Registrar Pago');
const deletePaymentId = ref<number | null>(null);

const formData = ref({
  id: null as number | null,
  patient: '',
  appointmentDate: '',
  totalAmount: 0,
  paymentType: '',
  paymentDate: new Date().toISOString().split('T')[0],
  password: ''
});

// Computed
const totalPagadoHoy = computed(() => {
  const hoy = new Date().toISOString().split('T')[0];
  return payments.value
      .filter(p => p.paymentDate === hoy && p.paymentType !== 'pending')
      .reduce((sum, p) => {
        return sum + (p.paymentType === 'advance' ? p.totalAmount * 0.5 : p.totalAmount);
      }, 0);
});

const citasConAnticipo = computed(() => {
  return payments.value.filter(p => p.paymentType === 'advance').length;
});

const pendientesPago = computed(() => {
  return payments.value.filter(p => p.paymentType !== 'complete').length;
});

// Funciones
const calcularAnticipo = (payment: any) => {
  return payment.paymentType === 'advance'
      ? payment.totalAmount * 0.5
      : (payment.paymentType === 'complete' ? payment.totalAmount : 0);
};

const calcularSaldo = (payment: any) => {
  const anticipo = calcularAnticipo(payment);
  return payment.paymentType === 'complete' ? 0 : payment.totalAmount - anticipo;
};

const getStatusText = (payment: any) => {
  if (payment.paymentType === 'complete') return 'Pagado';
  if (payment.paymentType === 'advance') return 'Pago Parcial';
  return 'Pendiente';
};

const getStatusClass = (payment: any) => {
  if (payment.paymentType === 'complete') return 'status-paid';
  if (payment.paymentType === 'advance') return 'status-partial';
  return 'status-pending';
};

const formatDate = (dateString: string) => {
  if (!dateString) return 'N/A';
  const date = new Date(dateString);
  date.setDate(date.getDate() + 1);
  return date.toLocaleDateString('es-ES');
};

const abrirModal = (paymentId: number | null = null) => {
  if (paymentId) {
    const payment = payments.value.find(p => p.id === paymentId);
    if (payment) {
      modalTitle.value = 'Editar Pago';
      isEditing.value = true;
      formData.value = {
        id: payment.id,
        patient: payment.patient,
        appointmentDate: payment.appointmentDate,
        totalAmount: payment.totalAmount,
        paymentType: payment.paymentType,
        paymentDate: payment.paymentDate || new Date().toISOString().split('T')[0],
        password: ''
      };
    }
  } else {
    modalTitle.value = 'Registrar Pago';
    isEditing.value = false;
    formData.value = {
      id: null,
      patient: '',
      appointmentDate: '',
      totalAmount: 0,
      paymentType: '',
      paymentDate: new Date().toISOString().split('T')[0],
      password: ''
    };
  }
  showModal.value = true;
};

const cerrarModal = () => {
  showModal.value = false;
};

const guardarPago = () => {
  if (isEditing.value && !formData.value.password) {
    alert('Por favor, ingrese la contraseña para guardar los cambios.');
    return;
  }

  if (formData.value.id) {
    // Editar
    const index = payments.value.findIndex(p => p.id === formData.value.id);
    if (index !== -1) {
      payments.value[index] = {
        id: formData.value.id,
        patient: formData.value.patient,
        appointmentDate: formData.value.appointmentDate,
        totalAmount: formData.value.totalAmount,
        paymentType: formData.value.paymentType,
        paymentDate: formData.value.paymentDate
      };
    }
  } else {
    // Agregar
    const newId = payments.value.length > 0
        ? Math.max(...payments.value.map(p => p.id)) + 1
        : 1;
    payments.value.push({
      id: newId,
      patient: formData.value.patient,
      appointmentDate: formData.value.appointmentDate,
      totalAmount: formData.value.totalAmount,
      paymentType: formData.value.paymentType,
      paymentDate: formData.value.paymentDate
    });
  }

  cerrarModal();
  alert(`Pago ${isEditing.value ? 'actualizado' : 'registrado'} exitosamente`);
};

const completarPago = (paymentId: number) => {
  if (confirm('¿Está seguro de marcar este pago como completo?')) {
    const index = payments.value.findIndex(p => p.id === paymentId);
    if (index !== -1 && payments.value[index].paymentType === 'advance') {
      payments.value[index].paymentType = 'complete';
      payments.value[index].paymentDate = new Date().toISOString().split('T')[0];
      alert('Pago completado exitosamente');
    }
  }
};

const confirmarEliminar = (paymentId: number) => {
  deletePaymentId.value = paymentId;
  showConfirmModal.value = true;
};

const cerrarConfirmModal = () => {
  showConfirmModal.value = false;
  deletePaymentId.value = null;
};

const eliminarPago = () => {
  if (deletePaymentId.value) {
    payments.value = payments.value.filter(p => p.id !== deletePaymentId.value);
    cerrarConfirmModal();
    alert('Pago eliminado exitosamente');
  }
};
</script>

<style scoped>
@import '@/style_gestion_de_pago.css';

/* Estilos adicionales específicos del componente */
.payment-container {
  max-width: 1200px;
  margin: 0 auto;
}
</style>