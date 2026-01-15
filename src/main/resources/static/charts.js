function renderMoodChart(entries) {
  if (!entries || entries.length === 0) return;

  var labels = entries.map((e) => e.date);
  var dataPoints = entries.map((e) => e.moodLevel);

  var ctx = document.getElementById("moodChart").getContext("2d");
  new Chart(ctx, {
    type: "line",
    data: {
      labels: labels,
      datasets: [
        {
          label: "Mood Level",
          data: dataPoints,
          borderColor: "#D35400",
          backgroundColor: "rgba(211, 84, 0, 0.2)",
          borderWidth: 2,
          fill: true,
          tension: 0.4,
        },
      ],
    },
    options: {
      scales: { y: { beginAtZero: true, max: 5 } },
    },
  });
}
