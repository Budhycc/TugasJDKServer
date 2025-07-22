let serviceTypes = [];
let services = [];

function buildTable(services) {
  const table = document.getElementById("table-services");
  const tbody = table.querySelector("tbody");
  tbody.replaceChildren();
  let i = 1;
  services.forEach((service) => {
    const row = document.createElement("tr");
    row.innerHTML = `
      <td>${i}</td>
      <td>${service.description}</td>
      <td>${service.amount}</td>
      <td>${service.date}</td>
      <td>${getServiceTypeName(service.serviceTypeId)}</td>
      <td>${service.customerName}</td>
      <td>${service.vehicleType}</td>
      <td>${service.licensePlate}</td>
      <td>
        <button class="btn btn-sm btn-warning me-2" onclick="showEditForm(${service.id})">Edit</button>
        <button class="btn btn-sm btn-danger" onclick="deleteService(${service.id})">Delete</button>
      </td>
    `;
    tbody.appendChild(row);
    i++;
  });
}

function getServiceTypeName(serviceTypeId) {
  const serviceType = serviceTypes.find((st) => st.id === serviceTypeId);
  return serviceType ? serviceType.name : "Unknown";
}

function populateServiceTypes() {
  const select = document.getElementById("service-type");
  select.replaceChildren();
  serviceTypes.forEach((serviceType) => {
    const option = document.createElement("option");
    option.value = serviceType.id;
    option.textContent = serviceType.name;
    select.appendChild(option);
  });
}

function showEditForm(id) {
  const service = services.find((s) => s.id === id);
  document.getElementById("id").value = service.id;
  document.getElementById("description").value = service.description;
  document.getElementById("amount").value = service.amount;
  document.getElementById("date").value = service.date;
  document.getElementById("service-type").value = service.serviceTypeId;
  document.getElementById("customer-name").value = service.customerName;
  document.getElementById("vehicle-type").value = service.vehicleType;
  document.getElementById("license-plate").value = service.licensePlate;

  document.getElementById("btn-save").textContent = "Update";
  document.getElementById("form-modal").dataset.mode = "update";

  const modal = new bootstrap.Modal(document.getElementById("form-modal"));
  modal.show();
}

function deleteService(id) {
  if (confirm("Are you sure you want to delete this service?")) {
    fetch(`/api/services/${id}`, { method: "DELETE" }).then(() => {
      loadData();
    });
  }
}

function loadData() {
  Promise.all([
    fetch("/api/servicetypes").then((res) => res.json()),
    fetch("/api/services").then((res) => res.json()),
    fetch("/api/reports?type=daily").then((res) => res.json()),
    fetch("/api/reports?type=weekly").then((res) => res.json()),
    fetch("/api/reports?type=monthly").then((res) => res.json()),
    fetch("/api/reports?type=yearly").then((res) => res.json()),
  ]).then(([sts, srvs, daily, weekly, monthly, yearly]) => {
    serviceTypes = sts;
    services = srvs;
    populateServiceTypes();
    buildTable(services);
    displayReport(daily, "daily-report");
    displayReport(weekly, "weekly-report");
    displayReport(monthly, "monthly-report");
    displayReport(yearly, "yearly-report");
  });
}

function displayReport(reportData, elementId) {
  const reportElement = document.getElementById(elementId);
  reportElement.innerHTML = "";
  for (const [key, value] of Object.entries(reportData)) {
    reportElement.innerHTML += `<div>${key}: ${value}</div>`;
  }
}

document.getElementById("btn-save").addEventListener("click", (e) => {
  const formModal = document.getElementById("form-modal");
  const mode = formModal.dataset.mode;
  const id = document.getElementById("id").value;

  const data = {
    description: document.getElementById("description").value,
    amount: document.getElementById("amount").value,
    date: document.getElementById("date").value,
    serviceTypeId: document.getElementById("service-type").value,
    customerName: document.getElementById("customer-name").value,
    vehicleType: document.getElementById("vehicle-type").value,
    licensePlate: document.getElementById("license-plate").value,
  };

  let method = "POST";
  let url = "/api/services";
  if (mode === "update") {
    method = "PUT";
    data.id = id;
  }

  fetch(url, {
    method: method,
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(data),
  })
    .then(() => {
      loadData();
      const modal = bootstrap.Modal.getInstance(formModal);
      modal.hide();
      formModal.dataset.mode = "create";
      document.getElementById("btn-save").textContent = "Save";
      document.getElementById("id").value = "";
      document.getElementById("description").value = "";
      document.getElementById("amount").value = "";
      document.getElementById("date").value = "";
      document.getElementById("service-type").value = "";
      document.getElementById("customer-name").value = "";
      document.getElementById("vehicle-type").value = "";
      document.getElementById("license-plate").value = "";
    });
});

loadData();
