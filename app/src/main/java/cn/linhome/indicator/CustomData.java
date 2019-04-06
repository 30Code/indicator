package cn.linhome.indicator;

public class CustomData
{
    private String url;
    private String name;
    private boolean isMovie;

    public CustomData(String url, String name, boolean isMovie)
    {
        this.url = url;
        this.name = name;
        this.isMovie = isMovie;
    }

    public boolean isMovie()
    {
        return isMovie;
    }

    public void setMovie(boolean movie)
    {
        isMovie = movie;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
