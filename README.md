#**Parking Data**
> - Httpconnecion을 사용하여 API를 통해 주차장 정보를 가져온다

##AsyncTask
```
new AsyncTask<String,Void,String>() {
            ProgressDialog dialog = new ProgressDialog(obj.getContext());
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog.setMessage("불러오는중.....");
                dialog.show();
            }

            @Override
            protected String doInBackground(String... params) {
                String urlString = params[0];

                try {
                    URL url = new URL(urlString);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET"); // GET : 데이터 요청시 사용하는 방식
                    int responseCode = connection.getResponseCode();
                    if(responseCode ==HttpURLConnection.HTTP_OK){
                        // 연결로 부터 스트림을 얻고, 버퍼래퍼로 감싼다.
                        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder result = new StringBuilder();
                        String lineOfdata = "";
                        String title = "";
                        // 번복문을 돌며넛 버퍼의 데이터를 읽어온다.
                        while((lineOfdata = br.readLine()) != null) {
                            result.append(lineOfdata);
                        }
                        return result.toString();
                    } else {
                        Log.e("HTTPConnection","Error Code "+ responseCode);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                Log.i("REMOTE", result);
                dialog.dismiss();
                obj.call(result);
            }
        }.execute(urlString);
```
