import { useState } from 'react';

function BarChart({ data }) {
  const [chartData] = useState(data);
  const width = 500;
  const height = 300;
  const barWidth = width / chartData.length;

  const maxValue = Math.max(...chartData);

  return (
    <svg width={width} height={height}>
      {chartData.map((value, index) => {
        const barHeight = (value / maxValue) * height;
        return (
          <rect
            key={index}
            x={index * barWidth}
            y={height - barHeight}
            width={barWidth - 2}
            height={barHeight}
            fill="steelblue"
          />
        );
      })}
    </svg>
  );
}

export default BarChart;