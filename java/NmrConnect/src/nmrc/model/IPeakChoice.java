package nmrc.model;

/*******************************************************************************
 * 
 ******************************************************************************/
public interface IPeakChoice
{
    /***************************************************************************
     * @return
     **************************************************************************/
    public IPeak getPeak();

    /***************************************************************************
     * @return
     **************************************************************************/
    public Double[] getAlphaMatch();

    /***************************************************************************
     * @return
     **************************************************************************/
    public Double[] getBetaMatch();
}
