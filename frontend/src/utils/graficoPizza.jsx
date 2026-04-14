function PieChart({ data }) {
  const width = 300;
  const height = 300;
  const radius = Math.min(width, height) / 2;
  const centerX = width / 2;
  const centerY = height / 2;

  const total = data.reduce((sum, value) => sum + value, 0);
  let startAngle = 0;

  return (
    <svg width={width} height={height}>
      {data.map((value, index) => {
        const angle = (value / total) * 360;
        const endAngle = startAngle + angle;
        const startRadians = (startAngle - 90) * (Math.PI / 180);
        const endRadians = (endAngle - 90) * (Math.PI / 180);

        const x1 = centerX + radius * Math.cos(startRadians);
        const y1 = centerY + radius * Math.sin(startRadians);
        const x2 = centerX + radius * Math.cos(endRadians);
        const y2 = centerY + radius * Math.sin(endRadians);

        const largeArcFlag = angle > 180 ? 1 : 0;

        const pathData = [
          `M ${centerX} ${centerY}`,
          `L ${x1} ${y1}`,
          `A ${radius} ${radius} 0 ${largeArcFlag} 1 ${x2} ${y2}`,
          'Z',
        ].join(' ');

        startAngle = endAngle;
        return <path key={index} d={pathData} fill={`hsl(${index * 60}, 70%, 50%)`} />;
      })}
    </svg>
  );
}

export default PieChart;