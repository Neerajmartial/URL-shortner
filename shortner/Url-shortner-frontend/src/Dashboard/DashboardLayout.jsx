import React, { useState, useEffect } from 'react';
import Graph from './Graph';
import api from '../api/api';
import toast from 'react-hot-toast';
import { FiCopy, FiExternalLink } from 'react-icons/fi';

const DashboardLayout = () => {
  const [links, setLinks] = useState([]);
  const [totalClicks, setTotalClicks] = useState([]);
  const [originalUrl, setOriginalUrl] = useState('');
  const [loading, setLoading] = useState(false);
  const [fetching, setFetching] = useState(true);

  const fetchLinks = async () => {
    try {
      const { data } = await api.get('/api/urls/myurls');
      setLinks(data);
    } catch (error) {
      toast.error('Failed to fetch your links');
    } finally {
      setFetching(false);
    }
  };

  const fetchAnalytics = async () => {
    try {
      const end = new Date();
      const start = new Date();
      start.setDate(start.getDate() - 7);
      
      const startDate = start.toISOString().split('T')[0];
      const endDate = end.toISOString().split('T')[0];

      const { data } = await api.get(`/api/urls/totalClicks?startDate=${startDate}&endDate=${endDate}`);
      
      const dates = [];
      for (let i = 0; i <= 7; i++) {
        const d = new Date(start);
        d.setDate(d.getDate() + i);
        dates.push(d.toISOString().split('T')[0]);
      }
      
      const formattedData = dates.map(date => ({
        clickDate: date,
        count: data[date] || 0
      }));

      setTotalClicks(formattedData);
    } catch (error) {
      console.error('Failed to fetch analytics:', error);
    }
  };

  useEffect(() => {
    fetchLinks();
    fetchAnalytics();
  }, []);

  const handleCreate = async (e) => {
    e.preventDefault();
    let finalUrl = originalUrl.trim();
    if (!finalUrl) {
      toast.error('Please enter a valid URL');
      return;
    }
    
    // Auto-prepend https:// if the user forgot it, to prevent invalid URL errors
    if (!/^https?:\/\//i.test(finalUrl)) {
      finalUrl = 'https://' + finalUrl;
    }

    try {
      setLoading(true);
      const { data } = await api.post('/api/urls/shorten', { originalUrl: finalUrl });
      toast.success('Link successfully shortened!');
      setOriginalUrl('');
      setLinks([{...data, clickCount: data.ClickCount || data.clickCount || 0}, ...links]);
    } catch (error) {
      toast.error('Error creating link');
    } finally {
      setLoading(false);
    }
  };

  const copyToClipboard = (shortUrl) => {
    const fullUrl = `http://localhost:8080/${shortUrl}`;
    navigator.clipboard.writeText(fullUrl);
    toast.success('Copied to clipboard');
  };

  return (
    <div className="lg:px-14 sm:px-8 px-4 min-h-[calc(100vh-64px)] bg-gray-50 flex flex-col pt-10 pb-20">
      <div className="w-full max-w-6xl mx-auto space-y-8">
        
        {/* Create Link Section */}
        <div className="bg-white p-6 md:p-8 rounded-2xl shadow-sm border border-gray-100 transition-all hover:shadow-md">
          <h2 className="text-2xl font-bold text-gray-800 mb-6 flex items-center gap-2">
            🔗 Create New Link
          </h2>
          <form onSubmit={handleCreate} className="flex flex-col md:flex-row gap-4">
            <div className="flex-1">
              <input
                type="text"
                value={originalUrl}
                onChange={(e) => setOriginalUrl(e.target.value)}
                placeholder="Paste your long URL here (e.g. example.com/very/long/path)"
                className="w-full px-5 py-4 bg-gray-50 border border-gray-200 rounded-xl focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition-all text-gray-800"
                required
              />
            </div>
            <button
              type="submit"
              disabled={loading}
              className="px-8 py-4 bg-blue-600 hover:bg-blue-700 text-white font-semibold rounded-xl transition-all shadow-sm disabled:opacity-70 flex items-center justify-center min-w-[160px]"
            >
              {loading ? 'Shortening...' : 'Shorten Link'}
            </button>
          </form>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
            {/* Graph Section */}
            <div className="bg-white p-6 rounded-2xl shadow-sm border border-gray-100 flex flex-col h-[400px]">
                <h3 className="text-lg font-bold text-gray-800 mb-4">Click Analytics (Past 7 Days)</h3>
                <div className="flex-1 relative w-full h-full">
                    <Graph graphData={totalClicks}/>
                </div>
            </div>

            {/* My Links Section */}
            <div className="bg-white p-6 rounded-2xl shadow-sm border border-gray-100 flex flex-col h-[400px]">
                <h3 className="text-lg font-bold text-gray-800 mb-4 flex justify-between items-center">
                   <span>My Shortened Links</span>
                   <span className="text-sm font-medium text-blue-600 bg-blue-50 px-3 py-1 rounded-full">{links.length} Links</span>
                </h3>
                <div className="flex-1 overflow-y-auto pr-2 space-y-4 beautiful-scrollbar">
                    {fetching ? (
                        <div className="flex justify-center items-center h-full text-gray-400">Loading your links...</div>
                    ) : links.length === 0 ? (
                        <div className="flex flex-col justify-center items-center h-full text-gray-400 space-y-2">
                           <div className="text-4xl">📭</div>
                           <p>No links found. Create one above!</p>
                        </div>
                    ) : (
                        links.map((link, idx) => (
                          <div key={link.id || idx} className="p-4 border border-gray-100 rounded-xl bg-gray-50 hover:bg-white transition-all group flex flex-col gap-2">
                              <div className="flex justify-between items-start">
                                  <a href={`http://localhost:8080/${link.shortUrl}`} target="_blank" rel="noreferrer" className="text-blue-600 font-semibold hover:underline flex items-center gap-1">
                                      http://localhost:8080/{link.shortUrl} <FiExternalLink size={14}/>
                                  </a>
                                  <div className="flex items-center gap-2">
                                     <span className="text-xs font-semibold bg-gray-200 text-gray-700 px-2 py-1 rounded-md">
                                        {link.clickCount || link.ClickCount || 0} Clicks
                                     </span>
                                     <button 
                                        onClick={() => copyToClipboard(link.shortUrl)}
                                        className="p-1.5 text-gray-400 hover:text-blue-600 hover:bg-blue-50 rounded-md transition-colors"
                                        title="Copy Link"
                                     >
                                         <FiCopy size={16}/>
                                     </button>
                                  </div>
                              </div>
                              <p className="text-sm text-gray-500 truncate" title={link.originalUrl}>
                                  {link.originalUrl}
                              </p>
                          </div>
                        ))
                    )}
                </div>
            </div>
        </div>

      </div>
    </div>
  );
};

export default DashboardLayout;