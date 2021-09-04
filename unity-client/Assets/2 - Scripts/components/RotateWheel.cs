using System;
using UnityEngine;
using UnityEngine.Events;

public class RotateWheel : MonoBehaviour
{
	private string[] slicePrizes = new string[] {
		"A KEY!!!", "50 STARS", "500 STARS", "BAD LUCK!!!",
		"200 STARS", "100 STARS", "150 STARS", "BAD LUCK!!!" };
	private const float MIN_SPEED = 10.0f;
	private const float MAX_SPEED = 800.0f;
	private float currentAngle = 0.0f;
	private float totalAngle;

	private float slices = 8;
	private float speed;
	private int prize;

	private bool enable = false;

	public bool Enable { get => enable; }

	public UnityEvent<string> finishEvent;

	void Start()
	{
		Deactivate();
	}

	/**
	 * Stop rotating the wheel
	 */
	public void Deactivate()
	{
		enable = false;
	}

	/**
	 * Start rotating the wheel,
	 * the wheel point to prize when it stop
	 */
	public void Activate(int prize)
	{
		this.prize = prize;
		Debug.Log("Prize: " + prize + ", prize name = " + slicePrizes[prize]);
		ComputeAngles(prize);
		enable = true;
	}

	void Update()
	{
		if (enable)
		{
			Rotate();
		}
	}

	/**
	 * Compute current angle and target angle
	 */
	private void ComputeAngles(int prize)
	{
		var rounds = UnityEngine.Random.Range(3, 6);

		float degrees = prize * 360 / slices;
		totalAngle = 360 * rounds + (360 - degrees);

		currentAngle = 360 - GetAbsoluteAngle(this.transform.localRotation.eulerAngles.z);
	}

	private float GetAbsoluteAngle(float angle)
	{
		float ans = angle;
		if (ans < 0)
		{
			ans += 360;
		}

		return ans;
	}

	private void Rotate()
	{
		float gap = totalAngle - currentAngle;
		if (gap > 0)
		{
			RotateByGap(gap);
		}
	}

	private void RotateByGap(float gap)
	{
		speed = Mathf.Max(gap, MIN_SPEED);
		speed = Mathf.Min(speed, MAX_SPEED);

		// Determine current step
		float step = speed * Time.deltaTime;

		if (currentAngle + step >= totalAngle)
		{
			step = totalAngle - currentAngle;
		}

		// Rotate the transform
		RotateTransform(step);

		// Accumulate angles
		currentAngle += step;

		if (currentAngle >= totalAngle)
		{
			Deactivate();
			finishEvent?.Invoke(slicePrizes[this.prize]);
		}
	}

	private void RotateTransform(float step)
	{
		transform.Rotate(-Vector3.forward * step);
	}
}