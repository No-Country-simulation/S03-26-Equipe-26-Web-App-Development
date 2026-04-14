function BarChart({ data, labels, levels = [] }) {
  const width = 500;
  const height = 400;
  const barWidth = 40;
  const gap = 20;
  const legendWidth = 150;
  const maxValue = Math.max(...data);

  return (
    <svg width={width + legendWidth} height={height}>
      {data.map((value, index) => {
        const barHeight = (value / maxValue) * (height - 100);
        const x = 50 + index * (barWidth + gap);
        const y = height - barHeight - 50;
        const isLevel = levels.includes(index);

        return (
          <g key={index}>
            <rect
              x={x}
              y={y}
              width={barWidth}
              height={barHeight}
              fill={isLevel ? 'hsl(200, 70%, 50%)' : `hsl(${index * 60}, 70%, 50%)`}
              stroke="#333"
              strokeWidth="1"
            />
            <text x={x + barWidth / 2} y={y - 5} textAnchor="middle" fill="black" fontSize="12">
              {value}
            </text>
            <text x={x + barWidth / 2} y={height - 30} textAnchor="middle" fill="black" fontSize="10">
              {/* {labels[index]} */}
            </text>
          </g>
        );
      })}
      <g transform={`translate(${width - 100}, 50)`}>
        {labels.map((label, index) => (
          <g key={index} transform={`translate(0, ${index * 20})`}>
            <rect width="12" height="12" fill={`hsl(${index * 60}, 70%, 50%)`} />
            <text x="20" y="12" fontSize="12">{label}</text>
          </g>
        ))}
      </g>
    </svg>
  );
}

export default BarChart;