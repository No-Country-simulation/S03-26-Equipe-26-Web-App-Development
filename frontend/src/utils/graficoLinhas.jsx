function LineChart({ data }) {
  const width = 500;
  const height = 300;
  const points = data
    .map((value, index) => {
      const x = (index / (data.length - 1)) * width;
      const y = height - (value / Math.max(...data)) * height;
      return `${x},${y}`;
    })
    .join(' ');

  return (
    <svg width={width} height={height}>
      <polyline
        fill="none"
        stroke="green"
        strokeWidth="2"
        points={points}
      />
    </svg>
  );
}

export default LineChart;