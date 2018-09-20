using UnityEngine;
using System.Collections;

public class Butterfly : MonoBehaviour {

	// Use this for initialization
	void Start () {
        StartCoroutine(Fly());
        if (GetComponent<Animator>())
        {
            GetComponent<Animator>().CrossFade( "walk", 0.1f,0,0.1f);
        }
	}
	
    IEnumerator Fly()
    {
        while(true)
        {
            if (GetComponent<Animator>())
            {
                if(Random.Range(1,5)>3)
                    GetComponent<Animator>().CrossFade("walk", 0.1f, 0, 0.1f);
                else
                    GetComponent<Animator>().CrossFade("stand", 0.1f, 0, 0.1f);
            }
            yield return new WaitForSeconds(Random.Range(3.0f, 10.0f));
        }
        yield break;
    }
}
