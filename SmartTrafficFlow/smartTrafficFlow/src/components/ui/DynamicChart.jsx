import React, { useState, useEffect } from 'react';
import {
  LineChart, Line, BarChart, Bar, AreaChart, Area,
  XAxis, YAxis, CartesianGrid, Tooltip, Legend,
  ResponsiveContainer, PieChart, Pie, Cell
} from 'recharts';

const COLORS = ['#3b82f6', '#10b981', '#f59e0b', '#ef4444', '#8b5cf6'];

export const DynamicChart = ({ data, type = 'bar', xKey = 'hora', yKey = 'volume', title, darkMode }) => {
  const [chartData, setChartData] = useState([]);

  useEffect(() => {
    if (data && Array.isArray(data)) {
      setChartData(data);
    }
  }, [data]);

  const renderChart = () => {
    const commonProps = {
      data: chartData,
      margin: { top: 5, right: 20, left: 0, bottom: 5 },
    };

    switch (type) {
      case 'line':
        return (
          <LineChart {...commonProps}>
            <CartesianGrid strokeDasharray="3 3" stroke={darkMode ? '#334155' : '#e2e8f0'} />
            <XAxis dataKey={xKey} stroke={darkMode ? '#94a3b8' : '#64748b'} fontSize={10} />
            <YAxis stroke={darkMode ? '#94a3b8' : '#64748b'} fontSize={10} />
            <Tooltip
              contentStyle={{
                backgroundColor: darkMode ? '#1e293b' : '#ffffff',
                borderColor: darkMode ? '#334155' : '#e2e8f0',
                color: darkMode ? '#ffffff' : '#1e293b'
              }}
            />
            <Legend />
            <Line type="monotone" dataKey={yKey} stroke="#3b82f6" strokeWidth={2} dot={{ r: 3 }} />
          </LineChart>
        );
      
      case 'area':
        return (
          <AreaChart {...commonProps}>
            <CartesianGrid strokeDasharray="3 3" stroke={darkMode ? '#334155' : '#e2e8f0'} />
            <XAxis dataKey={xKey} stroke={darkMode ? '#94a3b8' : '#64748b'} fontSize={10} />
            <YAxis stroke={darkMode ? '#94a3b8' : '#64748b'} fontSize={10} />
            <Tooltip
              contentStyle={{
                backgroundColor: darkMode ? '#1e293b' : '#ffffff',
                borderColor: darkMode ? '#334155' : '#e2e8f0'
              }}
            />
            <Area type="monotone" dataKey={yKey} stroke="#3b82f6" fill="#3b82f6" fillOpacity={0.3} />
          </AreaChart>
        );
      
      case 'pie':
        return (
          <PieChart>
            <Pie
              data={chartData}
              cx="50%"
              cy="50%"
              labelLine={false}
              label={({ name, percent }) => `${name}: ${(percent * 100).toFixed(0)}%`}
              outerRadius={80}
              fill="#8884d8"
              dataKey={yKey}
            >
              {chartData.map((entry, index) => (
                <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
              ))}
            </Pie>
            <Tooltip />
          </PieChart>
        );
      
      default:
        return (
          <BarChart {...commonProps}>
            <CartesianGrid strokeDasharray="3 3" stroke={darkMode ? '#334155' : '#e2e8f0'} />
            <XAxis dataKey={xKey} stroke={darkMode ? '#94a3b8' : '#64748b'} fontSize={10} />
            <YAxis stroke={darkMode ? '#94a3b8' : '#64748b'} fontSize={10} />
            <Tooltip
              contentStyle={{
                backgroundColor: darkMode ? '#1e293b' : '#ffffff',
                borderColor: darkMode ? '#334155' : '#e2e8f0'
              }}
            />
            <Bar dataKey={yKey} fill="#3b82f6" radius={[4, 4, 0, 0]}>
              {chartData.map((entry, index) => (
                <Cell key={`cell-${index}`} fill={entry[yKey] > 80 ? '#ef4444' : '#3b82f6'} />
              ))}
            </Bar>
          </BarChart>
        );
    }
  };

  return (
    <div className="w-full h-full">
      {title && (
        <h3 className="text-xs font-black text-slate-400 uppercase tracking-widest mb-4">
          {title}
        </h3>
      )}
      <ResponsiveContainer width="100%" height="100%">
        {renderChart()}
      </ResponsiveContainer>
    </div>
  );
};